package com.bornfire.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bornfire.services.UploadService;

@Controller
@ConfigurationProperties("default")
public class ASPIRAUploadController {
	private static final Logger logger = LoggerFactory.getLogger(ASPIRAUploadController.class);
	
	@Autowired
	UploadService UploadService;
	
	 @PostMapping(value = "/UploadCustomerData")
	 public ResponseEntity<Map<String, Object>> uploadExcel(@RequestParam("file") MultipartFile file,
	                                                           @RequestParam("fileInput") String fileInput,
	                                                           HttpServletRequest request,
	                                                           Model model, boolean overwrite)
	            throws FileNotFoundException, SQLException, IOException {

	        Map<String, Object> resultMap = new LinkedHashMap<>();

	        String userID = (String) request.getSession().getAttribute("USERID");
	        String userName = (String) request.getSession().getAttribute("USERNAME");

	        if ("CUSTOMER".equalsIgnoreCase(fileInput)) {
	            resultMap = UploadService.saveCustomerFile(file, userID, userName,overwrite);
	        } else if ("DESTINATION".equalsIgnoreCase(fileInput)) {
	            // resultMap = uploadService.saveDestinationFile(file, userID, userName, overwrite, fromDate);
	            resultMap.put("message", "Destination upload not implemented");
	        } else {
	            resultMap.put("message", "Invalid file type specified");
	        }

	        return ResponseEntity.ok(resultMap);
	    }
}
	


