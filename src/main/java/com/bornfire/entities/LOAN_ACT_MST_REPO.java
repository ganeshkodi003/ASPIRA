package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LOAN_ACT_MST_REPO extends JpaRepository<LOAN_ACT_MST_ENTITY, String> {
	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDet();

	@Query(value = "SELECT * \r\n" + 
			"FROM LOAN_ACCOUNT_MASTER_TBL \r\n" + 
			"WHERE ENCODED_KEY IN (\r\n" + 
			"    SELECT PARENT_ACCOUNT_KEY \r\n" + 
			"    FROM LOAN_REPAYMENT_TBL \r\n" + 
			"    WHERE PARENT_ACCOUNT_KEY IS NOT NULL\r\n" + 
			")", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActScd();
	
	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	LOAN_ACT_MST_ENTITY getLoanView(String id);

	@Query(value = "SELECT a.CUSTOMER_ID FROM CLIENT_MASTER_TBL a "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY "
			+ "WHERE b.ACCOUNT_HOLDERKEY = ?1", nativeQuery = true)
	List<String> getLoanValue(String holderKey);

	@Query(value = "SELECT a.CUSTOMER_ID, b.employer_name, b.CREATION_DATE, b.ID, b.INTEREST_RATE,  "
			+ "b.DISBURSEMENT_DATE, b.REPAYMENT_INSTALLMENTS, b.LOAN_AMOUNT,  " + "b.LOAN_NAME  "
			+ "FROM CLIENT_MASTER_TBL a  "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY WHERE A.ENCODED_KEY = ?1 and b.id = ?2", nativeQuery = true)
	Object[] getcustomer(String holder_key, String id);

	@Query(value = "SELECT a.ENCODED_KEY, B.DUE_DATE as dueDate, B.REPAID_DATE as repaidDate, " +  
            "B.PRINCIPAL_EXP as principalExp, B.PRINCIPAL_PAID as principalPaid, B.PRINCIPAL_DUE as principalDue, " +  
            "B.INTEREST_EXP as interestExp, B.INTEREST_PAID as interestPaid, B.INTEREST_DUE as interestDue, " +  
            "B.FEE_EXP as feeExp, B.FEE_PAID as feePaid, B.FEE_DUE as feeDue, " +  
            "B.PENALTY_EXP as penaltyExp, B.PENALTY_PAID as penaltyPaid, B.PENALTY_DUE as penaltyDue " +  
            "FROM LOAN_ACCOUNT_MASTER_TBL A " +  
            "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY " +  
            "WHERE A.ENCODED_KEY = ?1 " +  
            "ORDER BY B.DUE_DATE ASC", nativeQuery = true)
List<Object> getDues(String encodedKey);

	
	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL where  last_modified_date > approved_date", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActFilterUnverified();
	
	@Query(value = "SELECT CASE WHEN last_modified_date > approved_date THEN 1 ELSE 0 END " +
            "FROM LOAN_ACCOUNT_MASTER_TBL WHERE id = ?1", nativeQuery = true)
Integer getUnverifiedStatus(String id);

	@Query(value = "select id,loan_name,encoded_key from LOAN_ACCOUNT_MASTER_TBL order by id", nativeQuery = true)
	List<Object[]> getActNo();
	
	@Query(value = "SELECT encoded_key FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	String getLoanViewdatas(String id);

}
