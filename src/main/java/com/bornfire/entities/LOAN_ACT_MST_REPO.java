package com.bornfire.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LOAN_ACT_MST_REPO extends JpaRepository<LOAN_ACT_MST_ENTITY, String> {
	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActDet();

	@Query(value = "SELECT * \r\n" + "FROM LOAN_ACCOUNT_MASTER_TBL \r\n" + "WHERE ENCODED_KEY IN (\r\n"
			+ "    SELECT PARENT_ACCOUNT_KEY \r\n" + "    FROM LOAN_REPAYMENT_TBL \r\n"
			+ "    WHERE PARENT_ACCOUNT_KEY IS NOT NULL\r\n" + ")", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActScd();

	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	LOAN_ACT_MST_ENTITY getLoanView(String id);

	@Query(value = "SELECT a.CUSTOMER_ID FROM CLIENT_MASTER_TBL a "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY "
			+ "WHERE b.ACCOUNT_HOLDERKEY = ?1", nativeQuery = true)
	List<String> getLoanValue(String holderKey);

	@Query(value = "WITH ZeroBalanceDue AS ( " + "    SELECT TOP 1 DUE_DATE AS LAST_ZERO_BAL_DATE "
			+ "    FROM LOAN_REPAYMENT_TBL " + "    WHERE PARENT_ACCOUNT_KEY = ?4 "
			+ "    AND (PRINCIPAL_EXP - PRINCIPAL_PAID = 0 " + "         AND INTEREST_EXP - INTEREST_PAID = 0 "
			+ "         AND FEE_EXP - FEE_PAID = 0 " + "         AND PENALTY_EXP - PENALTY_PAID = 0) "
			+ "    AND DUE_DATE < ?1 " + "    ORDER BY DUE_DATE DESC " + "), " + "NextDueDate AS ( "
			+ "    SELECT TOP 1 DUE_DATE AS NEXT_DUE_DATE " + "    FROM LOAN_REPAYMENT_TBL "
			+ "    WHERE PARENT_ACCOUNT_KEY = ?4 "
			+ "    AND DUE_DATE > (SELECT LAST_ZERO_BAL_DATE FROM ZeroBalanceDue) " + "    ORDER BY DUE_DATE ASC "
			+ ") " + "SELECT " + "    a.CUSTOMER_ID, " + "    b.ENCODED_KEY, " + "    b.employer_name, "
			+ "    b.CREATION_DATE, " + "    b.ID, " + "    b.INTEREST_RATE, " + "    b.DISBURSEMENT_DATE, "
			+ "    b.REPAYMENT_INSTALLMENTS, " + "    b.LOAN_AMOUNT, " + "    b.LOAN_NAME, " + "    b.CURRENCY_CODE, "
			+ "    d.ACCT_BAL, " + "    zbd.LAST_ZERO_BAL_DATE, "
			+ "    DATEADD(DAY, 1, COALESCE(zbd.LAST_ZERO_BAL_DATE, ?1)) AS NEXT_DAY, " + "    nd.NEXT_DUE_DATE, "
			+ "    ?1 AS TRAN_DATE, "
			+ "    COALESCE(DATEDIFF(DAY, DATEADD(DAY, 1, COALESCE(zbd.LAST_ZERO_BAL_DATE, ?1)), ?1), 0) AS NO_OF_DAYS, "
			+ "    SUM( " + "        CASE "
			+ "            WHEN c.DUE_DATE <= ?1 THEN (c.PRINCIPAL_EXP + c.INTEREST_EXP + c.FEE_EXP) "
			+ "            ELSE 0 " + "        END " + "    ) AS DEMAND_APPLY, " + "    SUM( " + "        CASE "
			+ "            WHEN c.DUE_DATE <= ?1 " + "            AND (c.PRINCIPAL_EXP = c.PRINCIPAL_PAID) "
			+ "            AND (c.INTEREST_EXP = c.INTEREST_PAID) " + "            AND (c.FEE_EXP = c.FEE_PAID) "
			+ "            THEN (c.PRINCIPAL_PAID + c.INTEREST_PAID + c.FEE_PAID) " + "            ELSE 0 "
			+ "        END " + "    ) AS COLLECTION_APPLY, " + "    ( " + "        SUM( " + "            CASE "
			+ "                WHEN c.DUE_DATE <= ?1 THEN (c.PRINCIPAL_EXP + c.INTEREST_EXP + c.FEE_EXP) "
			+ "                ELSE 0 " + "            END " + "        ) - " + // Fixed subtraction error
			"        SUM( " + "            CASE " + "                WHEN c.DUE_DATE <= ?1 "
			+ "                AND (c.PRINCIPAL_EXP = c.PRINCIPAL_PAID) "
			+ "                AND (c.INTEREST_EXP = c.INTEREST_PAID) "
			+ "                AND (c.FEE_EXP = c.FEE_PAID) "
			+ "                THEN (c.PRINCIPAL_PAID + c.INTEREST_PAID + c.FEE_PAID) " + "                ELSE 0 "
			+ "            END " + "        ) " + "    ) AS ARREARS_APPLY " + "FROM CLIENT_MASTER_TBL a "
			+ "JOIN LOAN_ACCOUNT_MASTER_TBL b ON a.ENCODED_KEY = b.ACCOUNT_HOLDERKEY "
			+ "JOIN LOAN_REPAYMENT_TBL c ON b.ENCODED_KEY = c.PARENT_ACCOUNT_KEY "
			+ "JOIN BGLS_CHART_OF_ACCOUNTS d ON b.ID = d.ACCT_NUM " + "CROSS JOIN ZeroBalanceDue zbd " + // **Fixed JOIN
																											// condition**
			"CROSS JOIN NextDueDate nd " + // **Fixed JOIN condition**
			"WHERE a.ENCODED_KEY = ?2 " + "AND b.ID = ?3 " + "AND c.DUE_DATE <= ?1 " + "GROUP BY "
			+ "    a.CUSTOMER_ID, " + "    b.ENCODED_KEY, " + "    b.employer_name, " + "    b.CREATION_DATE, "
			+ "    b.ID, " + "    b.INTEREST_RATE, " + "    b.DISBURSEMENT_DATE, " + "    b.REPAYMENT_INSTALLMENTS, "
			+ "    b.LOAN_AMOUNT, " + "    b.LOAN_NAME, " + "    b.CURRENCY_CODE, " + "    d.ACCT_BAL, "
			+ "    zbd.LAST_ZERO_BAL_DATE, " + "    nd.NEXT_DUE_DATE", nativeQuery = true)
	Object[] getCustomer(Date tran_date, String holderKey, String id, String encodedKey);

	@Query(value = "SELECT a.ENCODED_KEY, B.DUE_DATE as dueDate, B.REPAID_DATE as repaidDate, "
			+ "B.PRINCIPAL_EXP as principalExp, B.PRINCIPAL_PAID as principalPaid, B.PRINCIPAL_DUE as principalDue, "
			+ "B.INTEREST_EXP as interestExp, B.INTEREST_PAID as interestPaid, B.INTEREST_DUE as interestDue, "
			+ "B.FEE_EXP as feeExp, B.FEE_PAID as feePaid, B.FEE_DUE as feeDue, "
			+ "B.PENALTY_EXP as penaltyExp, B.PENALTY_PAID as penaltyPaid, B.PENALTY_DUE as penaltyDue "
			+ "FROM LOAN_ACCOUNT_MASTER_TBL A " + "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE A.ENCODED_KEY = ?1 AND DEL_FLG = 'N'" + "ORDER BY B.DUE_DATE ASC", nativeQuery = true)
	List<Object> getDues(String encodedKey);

	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL where  last_modified_date > approved_date", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActFilterUnverified();

	@Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL where  last_modified_date < approved_date", nativeQuery = true)
	List<LOAN_ACT_MST_ENTITY> getLoanActFilterVerified();

	@Query(value = "SELECT CASE WHEN last_modified_date > approved_date THEN 1 ELSE 0 END "
			+ "FROM LOAN_ACCOUNT_MASTER_TBL WHERE id = ?1", nativeQuery = true)
	Integer getUnverifiedStatus(String id);

	@Query(value = "select id,loan_name,encoded_key from LOAN_ACCOUNT_MASTER_TBL order by id", nativeQuery = true)
	List<Object[]> getActNo();

	@Query(value = "SELECT encoded_key FROM LOAN_ACCOUNT_MASTER_TBL WHERE ID =?1", nativeQuery = true)
	String getLoanViewdatas(String id);
}
