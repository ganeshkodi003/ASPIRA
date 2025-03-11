package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface LOAN_ACT_MST_REPO extends JpaRepository<LOAN_ACT_MST_ENTITY, String>{
	 @Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL", nativeQuery = true)
	 List<LOAN_ACT_MST_ENTITY> getLoanActDet();
	 
	 @Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	 LOAN_ACT_MST_ENTITY getLoanView(String id);
	 
		@Query(value = "SELECT a.CUSTOMER_ID,b.employer_name, b.CREATION_DATE, b.ID, b.INTEREST_RATE, "
				+ "b.DISBURSEMENT_DATE, b.REPAYMENT_INSTALLMENTS, b.LOAN_AMOUNT " + "FROM CLIENT_MASTER_TBL a "
				+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY "
				+ "WHERE b.ACCOUNT_HOLDERKEY = ?1", nativeQuery = true)
		Object[] getLoanValue(String holder_key);
		
		@Query(value = "SELECT a.CUSTOMER_ID,b.employer_name, b.CREATION_DATE, b.ID, b.INTEREST_RATE,  \r\n"
				+ "	 b.DISBURSEMENT_DATE, b.REPAYMENT_INSTALLMENTS, b.LOAN_AMOUNT  ,\r\n"
				+ "	 b.LOAN_NAME  \r\n"
				+ "	 FROM CLIENT_MASTER_TBL a  \r\n"
				+ "	 JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY  \r\n"
				+ "	 WHERE b.ACCOUNT_HOLDERKEY = '8a858f5880dda0600180fa9d1aa307c8' AND B.ID ='CCR72dd23f96f062ac57953';", nativeQuery = true)
		Object[] getcustomer();
		
		
		@Query(value = "SELECT B.DUE_DATE ,B.REPAID_DATE , b.PRINCIPAL_DUE ,b.INTEREST_DUE\r\n"
				+ "	 FROM LOAN_ACCOUNT_MASTER_TBL a  \r\n"
				+ "	 JOIN LOAN_REPAYMENT_TBL b ON a.ENCODED_KEY = b.PARENT_ACCOUNT_KEY  \r\n"
				+ "	 WHERE a.ENCODED_KEY = '8a858e058fb3befa018fc311ca7131ac';", nativeQuery = true)
		List<Object> getDues();
		
		
		

}
