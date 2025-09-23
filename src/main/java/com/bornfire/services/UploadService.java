package com.bornfire.services;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.entities.CLIENT_MASTER_ENTITY;
import com.bornfire.entities.CLIENT_MASTER_REPO;

@Service
@ConfigurationProperties("output")
@Transactional
public class UploadService {
	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	CLIENT_MASTER_REPO clientMasterRepo;
	
	@Autowired
	DateParser DateParser;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int delteCustId(List<String> duplicateTr) {
		return clientMasterRepo.delteid(duplicateTr);
	}

	public Map<String, Object> saveCustomerFile(MultipartFile file, String userID, String userName,boolean overwrite) throws SQLException {
		int successCount = 0, failureCount = 0;
		Map<String, Object> resultMap = new LinkedHashMap<>();
		logger.info("Start 1");
		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			logger.info("Start 2");
			List<HashMap<Integer, String>> mapList = new ArrayList<>();
			for (Sheet s : workbook) {
			    for (Row r : s) {
			        if (!isRowEmpty(r)) {
			            if (r.getRowNum() < 1)
			                continue;

			            HashMap<Integer, String> map = new HashMap<>();
			            for (int j = 0; j < 200; j++) {
			                Cell cell = r.getCell(j);
			                DataFormatter formatter1 = new DataFormatter();
			                String text = formatter1.formatCellValue(cell);
			                map.put(j, text);
			            }
			            mapList.add(map);
			        }
			    }
			}
			logger.info("Start 3");
			// ✅ Pre-check duplicates check
			List<String> duplicateid = new ArrayList<>();
			for (HashMap<Integer, String> item : mapList) {
				String cust_id = item.get(1); // <-- taking ARN from column index 23
				// System.out.println(arn);
				CLIENT_MASTER_ENTITY checkId = clientMasterRepo.getid(cust_id);
				logger.info("Start 3.1");
				if (checkId != null) {
					duplicateid.add(cust_id);
				}
			}

			if (!duplicateid.isEmpty() && !overwrite) {
				resultMap.put("status", "duplicate");
				resultMap.put("id", duplicateid);
				return resultMap;
			}

			
			if (!duplicateid.isEmpty() && overwrite) {
				// delete existing before inserting
				delteCustId(duplicateid);
			}
			 
			//end duplicate check
			//upload start
			for (HashMap<Integer, String> item : mapList) {
				logger.info("Start 4");
				try {
					logger.info("Start 5");
					CLIENT_MASTER_ENTITY transaction = new CLIENT_MASTER_ENTITY();
					transaction.setEncoded_key(item.get(0));
					transaction.setCustomer_id(item.get(1));
					transaction.setClient_state(item.get(2));
					transaction.setCreation_date(DateParser.parseDateSafe(item.get(3)));
					transaction.setLast_modified_date(DateParser.parseDateSafe(item.get(4)));
					transaction.setActivation_date(DateParser.parseDateSafe(item.get(5)));
					transaction.setApproved_date(DateParser.parseDateSafe(item.get(6)));
					transaction.setFirst_name(item.get(7));
					transaction.setLast_name(item.get(8));
					transaction.setMobile_phone(item.get(9));
					transaction.setEmail_address(item.get(10));
					transaction.setPreferred_language(item.get(11));
					transaction.setBirth_date(DateParser.parseDateSafe(item.get(12)));
					transaction.setGender(item.get(13));
					transaction.setAssigned_branch_key(item.get(14));
					transaction.setClient_role_key(item.get(15));
					transaction.setLoan_cycle(DateParser.parseBigDecimal(item.get(16)));
					transaction.setGroup_loan_cycle(DateParser.parseBigDecimal(item.get(17)));
					transaction.setAddress_line1(item.get(18));
					transaction.setAddress_line2(item.get(19));
					transaction.setAddress_line3(item.get(20));
					transaction.setCity(item.get(21));
					transaction.setSuburb(item.get(22));
					transaction.setAssigned_user_key(item.get(23));
					transaction.setAsondate(DateParser.parseDateSafe(item.get(24)));
					transaction.setDel_flg("N");
					transaction.setEntry_user(userID);
					transaction.setEntry_time(new Date());
							
					logger.info("Start 7");
					clientMasterRepo.save(transaction); 
					successCount++;
					//System.out.println("FINAL COUNTS -> Succeeded: " + successCount + ", Failed: " + failureCount);
				} catch (Exception ex) {
					failureCount++;
					ex.printStackTrace();
				}
			}
			logger.info("Start 8");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("status", "error");
			resultMap.put("message", "File upload failed: " + e.getMessage());
		}
		logger.info("Start 9");	
		resultMap.put("status", "success");
		resultMap.put("TotalSucceeded", successCount);
		resultMap.put("TotalFailed", failureCount);
		resultMap.put("TotalProcessed", (successCount + failureCount));

		return resultMap;
	}
	
	private boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}



}
