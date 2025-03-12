package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LOAN_ACT_MST_REPO extends JpaRepository<LOAN_ACT_MST_ENTITY, String> {
	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDet();

	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	LOAN_ACT_MST_ENTITY getLoanView(String id);

	@Query(value = "SELECT a.CUSTOMER_ID, b.employer_name, b.CREATION_DATE, b.ID, b.INTEREST_RATE, "
			+ "b.DISBURSEMENT_DATE, b.REPAYMENT_INSTALLMENTS, b.LOAN_AMOUNT, b.account_holderkey "
			+ "FROM CLIENT_MASTER_TBL a "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY ", nativeQuery = true)
	List<Object[]> getLoanValue(String holderKey);

	@Query(value = "SELECT a.CUSTOMER_ID, b.employer_name, b.CREATION_DATE, b.ID, b.INTEREST_RATE,  "
			+ "b.DISBURSEMENT_DATE, b.REPAYMENT_INSTALLMENTS, b.LOAN_AMOUNT,  " + "b.LOAN_NAME  "
			+ "FROM CLIENT_MASTER_TBL a  "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY WHERE A.ENCODED_KEY = ?1 and b.id = ?2", nativeQuery = true)
	Object[] getcustomer(String holder_key, String id);

	@Query(value = "SELECT a.ENCODED_KEY,B.DUE_DATE as dueDate, B.REPAID_DATE as repaidDate,  \r\n"
			+ "                       B.PRINCIPAL_EXP as principalExp, B.PRINCIPAL_PAID as principalPaid, B.PRINCIPAL_DUE as principalDue,  \r\n"
			+ "                       B.INTEREST_EXP as interestExp, B.INTEREST_PAID as interestPaid, B.INTEREST_DUE as interestDue,  \r\n"
			+ "                       B.FEE_EXP as feeExp, B.FEE_PAID as feePaid, B.FEE_DUE as feeDue,  \r\n"
			+ "                       B.PENALTY_EXP as penaltyExp, B.PENALTY_PAID as penaltyPaid, B.PENALTY_DUE as penaltyDue  \r\n"
			+ "                FROM LOAN_ACCOUNT_MASTER_TBL A  \r\n"
			+ "                JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY  \r\n"
			+ "                WHERE A.ENCODED_KEY = ?1", nativeQuery = true)
	List<Object> getDues(String encodedKey);

}
