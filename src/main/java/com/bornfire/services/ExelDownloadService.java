package com.bornfire.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.bornfire.entities.ASPIRA_LOAN_REPAYMENT_ENTITY;
import com.bornfire.entities.ASPIRA_LOAN_REPAYMENT_REPO;
import com.bornfire.entities.BGLSAuditTable;
import com.bornfire.entities.BGLSAuditTable_Rep;
import com.bornfire.entities.CLIENT_MASTER_ENTITY;
import com.bornfire.entities.CLIENT_MASTER_REPO;
import com.bornfire.entities.LOAN_ACT_MST_ENTITY;
import com.bornfire.entities.LOAN_ACT_MST_REPO;

@Service
@ConfigurationProperties("output")
@Transactional
public class ExelDownloadService {
	private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	ASPIRA_LOAN_REPAYMENT_REPO lOAN_REPAYMENT_REPO;
	
	@Autowired
	LOAN_ACT_MST_REPO loanMasterRepo;
	
	@Autowired
	DateParser DateParser;
	
	@Autowired
	BGLSAuditTable_Rep AuditTable_Rep;
	

	public void ExportExcel(String type, String userID, String userName, String auditRefNo,
			HttpServletResponse response) {

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Data");
			int rowIdx = 0;

			if ("REPAYMENT".equalsIgnoreCase(type)) {
				List<ASPIRA_LOAN_REPAYMENT_ENTITY> dataList = lOAN_REPAYMENT_REPO.findAll();

				// Header
				Row header = sheet.createRow(rowIdx++);
				String[] headers = {
					    "ENCODEDKEY","ASSIGNEDBRANCHKEY","ASSIGNEDUSERKEY","DUEDATE","INTERESTDUE","INTERESTPAID","LASTPAIDDATE",
					    "LASTPENALTYAPPLIEDDATE", "NOTES","PARENTACCOUNTKEY","PRINCIPALDUE", "PRINCIPALPAID", "REPAIDDATE", "STATE",
					    "ASSIGNEDCENTREKEY","FEESDUE","FEESPAID","PENALTYDUE","PENALTYPAID","TAXINTERESTDUE","TAXINTERESTPAID",
					    "TAXFEESDUE", "TAXFEESPAID","TAXPENALTYDUE","TAXPENALTYPAID","ORGANIZATIONCOMMISSIONDUE",
					    "FUNDERSINTERESTDUE","CREATIONDATE","LASTMODIFIEDDATE","ADDITIONS"
					};

				for (int i = 0; i < headers.length; i++) {
					header.createCell(i).setCellValue(headers[i]);
				}
					System.out.println(dataList.size()+"   List Size");
				for (ASPIRA_LOAN_REPAYMENT_ENTITY entity : dataList) {
					Row excelRow = sheet.createRow(rowIdx++);

					// String values
					System.out.println(entity.getEncodedkey()+"  --Encode Key");
					excelRow.createCell(0).setCellValue(entity.getEncodedkey());
					excelRow.createCell(1).setCellValue(entity.getAssignedbranchkey());
					excelRow.createCell(2).setCellValue(entity.getAssigneduserkey());

					// Date values (format to String)
					excelRow.createCell(3).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDuedate()));

					// BigDecimal values (convert to String or double)
					excelRow.createCell(4).setCellValue(entity.getInterestdue() == null ? "" : entity.getInterestdue().toPlainString());
					excelRow.createCell(5).setCellValue(entity.getInterestpaid() == null ? "" : entity.getInterestpaid().toPlainString());

					// More dates
					excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpaiddate()));
					excelRow.createCell(7).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpenaltyapplieddate()));

					// Notes
					excelRow.createCell(8).setCellValue(entity.getNotes());
					excelRow.createCell(9).setCellValue(entity.getParentaccountkey());

					// Principal
					excelRow.createCell(10).setCellValue(entity.getPrincipaldue() == null ? "" : entity.getPrincipaldue().toPlainString());
					excelRow.createCell(11).setCellValue(entity.getPrincipalpaid() == null ? "" : entity.getPrincipalpaid().toPlainString());

					// Repaid date & state
					excelRow.createCell(12).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getRepaiddate()));
					excelRow.createCell(13).setCellValue(entity.getState());

					// Centre key
					excelRow.createCell(14).setCellValue(entity.getAssignedcentrekey());

					// Fees
					excelRow.createCell(15).setCellValue(entity.getFeesdue() == null ? "" : entity.getFeesdue().toPlainString());
					excelRow.createCell(16).setCellValue(entity.getFeespaid() == null ? "" : entity.getFeespaid().toPlainString());

					// Penalty
					excelRow.createCell(17).setCellValue(entity.getPenaltydue() == null ? "" : entity.getPenaltydue().toPlainString());
					excelRow.createCell(18).setCellValue(entity.getPenaltypaid() == null ? "" : entity.getPenaltypaid().toPlainString());

					// Tax Interest
					excelRow.createCell(19).setCellValue(entity.getTaxinterestdue() == null ? "" : entity.getTaxinterestdue().toPlainString());
					excelRow.createCell(20).setCellValue(entity.getTaxinterestpaid() == null ? "" : entity.getTaxinterestpaid().toPlainString());

					// Tax Fees
					excelRow.createCell(21).setCellValue(entity.getTaxfeesdue() == null ? "" : entity.getTaxfeesdue().toPlainString());
					excelRow.createCell(22).setCellValue(entity.getTaxfeespaid() == null ? "" : entity.getTaxfeespaid().toPlainString());

					// Tax Penalty
					excelRow.createCell(23).setCellValue(entity.getTaxpenaltydue() == null ? "" : entity.getTaxpenaltydue().toPlainString());
					excelRow.createCell(24).setCellValue(entity.getTaxpenaltypaid() == null ? "" : entity.getTaxpenaltypaid().toPlainString());

					// Org & Funders
					excelRow.createCell(25).setCellValue(entity.getOrganizationcommissiondue() == null ? "" : entity.getOrganizationcommissiondue().toPlainString());
					excelRow.createCell(26).setCellValue(entity.getFundersinterestdue() == null ? "" : entity.getFundersinterestdue().toPlainString());

					// Creation & Last modified
					excelRow.createCell(27).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreationdate()));
					excelRow.createCell(28).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastmodifieddate()));

					// Additions
					excelRow.createCell(29).setCellValue(entity.getAdditions());

				}

				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
				response.setHeader("Content-Disposition", "inline; filename=source_data.xlsx");

			}  else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type parameter");
				return;
			}

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			workbook.write(response.getOutputStream());

		} catch (Exception e) {
			try {
				if (!response.isCommitted()) {
					response.reset();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.setContentType("text/plain");
					response.getWriter().write("Error generating Excel: " + e.getMessage());
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void LoanMasterExportExcel(String userID, String userName, String auditRefNo,
			HttpServletResponse response) {

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Data");
			int rowIdx = 0;

//			if ("REPAYMENT".equalsIgnoreCase(type)) {
				List<LOAN_ACT_MST_ENTITY> dataList = loanMasterRepo.findAll();

				// Header
				Row header = sheet.createRow(rowIdx++);
				String[] headers = {
					    "ACCOUNTHOLDERTYPE","ACCOUNTHOLDERKEY","CREATIONDATE","APPROVEDDATE","LASTMODIFIEDDATE","CLOSEDDATE","LASTACCOUNTAPPRAISALDATE","ACCOUNTSTATE","ACCOUNTSUBSTATE",
					    "PRODUCTTYPEKEY","LOANNAME","PAYMENTMETHOD","ASSIGNEDBRANCHKEY","LOANAMOUNT","INTERESTRATE","PENALTYRATE","ACCRUEDINTEREST","ACCRUEDPENALTY","PRINCIPALDUE",
					    "PRINCIPALPAID","PRINCIPALBALANCE","INTERESTDUE","INTERESTPAID","INTERESTBALANCE","INTERESTFROMARREARSBALANCE","INTERESTFROMARREARSDUE","INTERESTFROMARREARSPAID",
					    "FEESDUE","FEESPAID","FEESBALANCE","PENALTYDUE","PENALTYPAID","PENALTYBALANCE","EXPECTEDDISBURSEMENTDATE","DISBURSEMENTDATE","FIRSTREPAYMENTDATE","GRACEPERIOD",
					    "REPAYMENTINSTALLMENTS","REPAYMENTPERIODCOUNT","DAYSLATE","DAYSINARREARS","REPAYMENTSCHEDULEMETHOD","CURRENCYCODE","SALEPROCESSEDBYVGID","SALEPROCESSEDFOR",
					    "SALEREFERREDBY","EMPLOYMENTSTATUS","JOBTITLE","EMPLOYERNAME","TUSCORE","TUPROBABILITY","TUFULLNAME","TUREASON1","TUREASON2","TUREASON3","TUREASON4",
					    "DISPOSABLEINCOME","MANUALOVERRIDEAMOUNT","MANUALOVERRIDEEXPIRYDATE","CPFEES","DEPOSITAMOUNT","TOTALPRODUCTPRICE","RETAILERNAME","RETAILERBRANCH","VGAPPLICATIONID",
					    "CONTRACTSIGNED","DATEOFFIRSTCALL","LASTCALLOUTCOME","ASONDATE"
					};

				for (int i = 0; i < headers.length; i++) {
					header.createCell(i).setCellValue(headers[i]);
				}
					System.out.println(dataList.size()+"   List Size of Loan Master");
				for (LOAN_ACT_MST_ENTITY entity : dataList) {
					Row excelRow = sheet.createRow(rowIdx++);

//					// String values
//					excelRow.createCell(0).setCellValue(entity.getAccount_holdertype());
//					excelRow.createCell(1).setCellValue(entity.getAssignedbranchkey());
//					excelRow.createCell(2).setCellValue(entity.getAssigneduserkey());
//
//					// Date values (format to String)
//					excelRow.createCell(3).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getDuedate()));
//
//					// BigDecimal values (convert to String or double)
//					excelRow.createCell(4).setCellValue(entity.getInterestdue() == null ? "" : entity.getInterestdue().toPlainString());
//					excelRow.createCell(5).setCellValue(entity.getInterestpaid() == null ? "" : entity.getInterestpaid().toPlainString());
//
//					// More dates
//					excelRow.createCell(6).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpaiddate()));
//					excelRow.createCell(7).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastpenaltyapplieddate()));
//
//					// Notes
//					excelRow.createCell(8).setCellValue(entity.getNotes());
//					excelRow.createCell(9).setCellValue(entity.getParentaccountkey());
//
//					// Principal
//					excelRow.createCell(10).setCellValue(entity.getPrincipaldue() == null ? "" : entity.getPrincipaldue().toPlainString());
//					excelRow.createCell(11).setCellValue(entity.getPrincipalpaid() == null ? "" : entity.getPrincipalpaid().toPlainString());
//
//					// Repaid date & state
//					excelRow.createCell(12).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getRepaiddate()));
//					excelRow.createCell(13).setCellValue(entity.getState());
//
//					// Centre key
//					excelRow.createCell(14).setCellValue(entity.getAssignedcentrekey());
//
//					// Fees
//					excelRow.createCell(15).setCellValue(entity.getFeesdue() == null ? "" : entity.getFeesdue().toPlainString());
//					excelRow.createCell(16).setCellValue(entity.getFeespaid() == null ? "" : entity.getFeespaid().toPlainString());
//
//					// Penalty
//					excelRow.createCell(17).setCellValue(entity.getPenaltydue() == null ? "" : entity.getPenaltydue().toPlainString());
//					excelRow.createCell(18).setCellValue(entity.getPenaltypaid() == null ? "" : entity.getPenaltypaid().toPlainString());
//
//					// Tax Interest
//					excelRow.createCell(19).setCellValue(entity.getTaxinterestdue() == null ? "" : entity.getTaxinterestdue().toPlainString());
//					excelRow.createCell(20).setCellValue(entity.getTaxinterestpaid() == null ? "" : entity.getTaxinterestpaid().toPlainString());
//
//					// Tax Fees
//					excelRow.createCell(21).setCellValue(entity.getTaxfeesdue() == null ? "" : entity.getTaxfeesdue().toPlainString());
//					excelRow.createCell(22).setCellValue(entity.getTaxfeespaid() == null ? "" : entity.getTaxfeespaid().toPlainString());
//
//					// Tax Penalty
//					excelRow.createCell(23).setCellValue(entity.getTaxpenaltydue() == null ? "" : entity.getTaxpenaltydue().toPlainString());
//					excelRow.createCell(24).setCellValue(entity.getTaxpenaltypaid() == null ? "" : entity.getTaxpenaltypaid().toPlainString());
//
//					// Org & Funders
//					excelRow.createCell(25).setCellValue(entity.getOrganizationcommissiondue() == null ? "" : entity.getOrganizationcommissiondue().toPlainString());
//					excelRow.createCell(26).setCellValue(entity.getFundersinterestdue() == null ? "" : entity.getFundersinterestdue().toPlainString());
//
//					// Creation & Last modified
//					excelRow.createCell(27).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getCreationdate()));
//					excelRow.createCell(28).setCellValue(DateParser.getCurrentDateWithoutTimePass(entity.getLastmodifieddate()));
//
//					// Additions
//					excelRow.createCell(29).setCellValue(entity.getAdditions());

				}

				saveAudit(userID, userName, "Repayment File Download!", "ASPIRA_LOAN_REPAYMENT_TABLE", auditRefNo);
				response.setHeader("Content-Disposition", "inline; filename=source_data.xlsx");

//			}  else {
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type parameter");
//				return;
//			}

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			workbook.write(response.getOutputStream());

		} catch (Exception e) {
			try {
				if (!response.isCommitted()) {
					response.reset();
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.setContentType("text/plain");
					response.getWriter().write("Error generating Excel: " + e.getMessage());
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void saveAudit(String userID, String userName, String remarks, String table, String refNo) {
		BGLSAuditTable audit = new BGLSAuditTable();
		audit.setAudit_date(new Date());
		audit.setEntry_time(new Date());
		audit.setEntry_user(userID);
		audit.setFunc_code("DOWNLOAD");
		audit.setRemarks(remarks);
		audit.setAudit_table(table);
		audit.setAudit_screen("UPLOAD");
		audit.setEvent_id(userID);
		audit.setEvent_name(userName);
		audit.setModi_details("-");
		audit.setAudit_ref_no(refNo);
		AuditTable_Rep.save(audit);
	}

}
