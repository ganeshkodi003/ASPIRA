package com.bornfire.entities;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LOAN_REPAYMENT_REPO extends JpaRepository<LOAN_REPAYMENT_ENTITY, String> {
	@Query(value = "SELECT B.due_date AS due_date, " + "       '1' AS flow_id, " + "       'INDEM' AS flow_code, "
			+ "       B.INTEREST_DUE AS flow_amt, " + "       A.ID AS loan_acct_no, "
			+ "       A.LOAN_NAME AS acct_name " + "FROM LOAN_ACCOUNT_MASTER_TBL A "
			+ "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE BETWEEN ?1 AND ?2 " + "AND B.PARENT_ACCOUNT_KEY = ?3 "
			+ "AND B.payment_state = 'PENDING' " + "AND B.due_date > '2025-01-17' " + // ✅ Hardcoded date filter
			"ORDER BY B.due_date", nativeQuery = true)
	List<Object[]> getloanflows(Date fromDate, Date toDate, String accountNum);

	@Query(value = "SELECT \r\n" + "    U.due_date,  \r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '1'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN '3'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'\r\n" + "    END AS flow_id,\r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + "    END AS flow_code,\r\n"
			+ "    U.flow_amt,\r\n" + "    U.loan_acct_no AS loan_acct_no,\r\n" + "    U.acct_name AS acct_name,\r\n"
			+ "    U.encoded_key \r\n" + "FROM \r\n" + "(\r\n" + "    SELECT \r\n" + "        B.due_date,  \r\n"
			+ "        B.PRINCIPAL_EXP,\r\n" + "        B.INTEREST_EXP,\r\n" + "        B.FEE_EXP,\r\n"
			+ "        B.PENALTY_EXP,\r\n" + "        A.ID AS loan_acct_no,              \r\n"
			+ "        A.LOAN_NAME AS acct_name,  \r\n" + "        A.ENCODED_KEY \r\n"
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n" + "    JOIN LOAN_REPAYMENT_TBL B \r\n"
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n" + "    WHERE \r\n"
			+ "        B.DUE_DATE BETWEEN :fromDate AND :toDate \r\n"
			+ "        AND B.PARENT_ACCOUNT_KEY = :accountNum\r\n" + "        AND B.payment_state = 'PENDING'\r\n"
			+ ") AS P\r\n" + "UNPIVOT \r\n" + "(\r\n"
			+ "    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + ") AS U\r\n"
			+ "WHERE U.flow_amt > 0 \r\n" + "ORDER BY U.due_date, flow_id", nativeQuery = true)
	List<Object[]> getloanflowsvalue(Date fromDate, Date toDate, String accountNum);

	@Query(value = "SELECT B.due_date AS due_date, " + "       '1' AS flow_id, " + "       'INDEM' AS flow_code, "
			+ "       B.INTEREST_DUE AS flow_amt, " + "       A.ID AS loan_acct_no, "
			+ "       A.LOAN_NAME AS acct_name, " + "       A.INTEREST_RATE AS interest_rate "
			+ "FROM LOAN_ACCOUNT_MASTER_TBL A " + "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE BETWEEN ?1 AND ?2 " + "AND B.PARENT_ACCOUNT_KEY = ?3 "
			+ "AND B.payment_state = 'PENDING' " + "AND B.due_date > '2025-01-17' " + // ✅ Now only showing dates //
																						// *after* 2025-01-17
			"ORDER BY B.due_date", nativeQuery = true)
	List<Object[]> getloanflowsatas(Date fromDate, Date toDate, String accountNum);

	@Query(value = "select * from LOAN_REPAYMENT_TBL where PARENT_ACCOUNT_KEY = ?1 and DUE_DATE = ?2", nativeQuery = true)
	LOAN_REPAYMENT_ENTITY getLoanFlowsValueDatas(String accountNum, String flowDate);

	@Query(value = "SELECT B.due_date AS due_date, " + "       '1' AS flow_id, " + "       'FEEDEM' AS flow_code, "
			+ "       B.FEE_DUE AS flow_amt, " + "       A.ID AS loan_acct_no, " + "       A.LOAN_NAME AS acct_name, "
			+ "       A.ENCODED_KEY AS encoded_key " + // ✅ Added encoded_key
			"FROM LOAN_ACCOUNT_MASTER_TBL A " + "JOIN LOAN_REPAYMENT_TBL B ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY "
			+ "WHERE B.DUE_DATE BETWEEN ?1 AND ?2 " + "AND B.PARENT_ACCOUNT_KEY = ?3 "
			+ "AND B.payment_state = 'PENDING' " + "AND B.due_date > '2025-01-17' " + // ✅ Added correct condition
			"ORDER BY B.due_date", nativeQuery = true)
	List<Object[]> getloanflowsdatas(Date fromDate, Date toDate, String accountNum);

	@Query(value = "select * from LOAN_REPAYMENT_TBL where PARENT_ACCOUNT_KEY = ?1 AND payment_state ='PENDING'", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatasVALUES(String accountNum);

	@Query(value = "SELECT \r\n" + "    U.due_date,  \r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '1'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN '3'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'\r\n" + "    END AS flow_id,\r\n" + "    CASE \r\n"
			+ "        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n"
			+ "        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n"
			+ "        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n"
			+ "        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + "    END AS flow_code,\r\n"
			+ "    U.flow_amt,\r\n" + "    U.loan_acct_no AS loan_acct_no,\r\n" + "    U.acct_name AS acct_name,\r\n"
			+ "    U.encoded_key \r\n" + "FROM \r\n" + "(\r\n" + "    SELECT \r\n" + "        B.due_date,  \r\n"
			+ "        B.PRINCIPAL_EXP,\r\n" + "        B.INTEREST_EXP,\r\n" + "        B.FEE_EXP,\r\n"
			+ "        B.PENALTY_EXP,\r\n" + "        A.ID AS loan_acct_no,              \r\n"
			+ "        A.LOAN_NAME AS acct_name,  \r\n" + "        A.ENCODED_KEY \r\n"
			+ "    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n" + "    JOIN LOAN_REPAYMENT_TBL B \r\n"
			+ "        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n" + "    WHERE \r\n"
			+ "       B.PARENT_ACCOUNT_KEY = :accountNum\r\n" + "        AND B.payment_state = 'PENDING'\r\n"
			+ ") AS P\r\n" + "UNPIVOT \r\n" + "(\r\n"
			+ "    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + ") AS U\r\n"
			+ "WHERE U.flow_amt > 0 \r\n" + "ORDER BY U.due_date, flow_id", nativeQuery = true)
	List<Object[]> getloanflowsvaluedatas(String accountNum);

	@Query(value = "SELECT \r\n" + 
			"    U.due_date,  \r\n" + 
			"    CASE \r\n" + 
			"        WHEN U.flow_type = 'FEE_EXP' THEN '1'         -- Fees First\r\n" + 
			"        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'    -- Interest Second\r\n" + 
			"        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '3'   -- Principal Third\r\n" + 
			"        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'     -- Penalty Last\r\n" + 
			"    END AS flow_id,\r\n" + 
			"    CASE \r\n" + 
			"        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n" + 
			"        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n" + 
			"        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n" + 
			"        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + 
			"    END AS flow_code,\r\n" + 
			"    U.flow_amt,\r\n" + 
			"    U.loan_acct_no,\r\n" + 
			"    U.acct_name,\r\n" + 
			"    U.encoded_key \r\n" + 
			"FROM (\r\n" + 
			"    SELECT \r\n" + 
			"        B.due_date,  \r\n" + 
			"        A.ID AS loan_acct_no,              \r\n" + 
			"        A.LOAN_NAME AS acct_name,  \r\n" + 
			"        A.ENCODED_KEY,\r\n" + 
			"        -- Calculate remaining due amount\r\n" + 
			"        B.PRINCIPAL_EXP - B.PRINCIPAL_PAID AS PRINCIPAL_EXP,\r\n" + 
			"        B.INTEREST_EXP - B.INTEREST_PAID AS INTEREST_EXP,\r\n" + 
			"        B.FEE_EXP - B.FEE_PAID AS FEE_EXP,\r\n" + 
			"        B.PENALTY_EXP - B.PENALTY_PAID AS PENALTY_EXP\r\n" + 
			"    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n" + 
			"    JOIN LOAN_REPAYMENT_TBL B \r\n" + 
			"        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n" + 
			"    WHERE \r\n" + 
			"        B.DUE_DATE <= ?1  -- Replace with actual date\r\n" + 
			"        AND B.PARENT_ACCOUNT_KEY = ?2 -- Replace with actual account number\r\n" + 
			"        AND B.payment_state = 'PENDING'\r\n" + 
			") P\r\n" + 
			"UNPIVOT (\r\n" + 
			"    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + 
			") AS U\r\n" + 
			"-- Only show amounts where remaining due > 0\r\n" + 
			"WHERE U.flow_amt > 0  \r\n" + 
			"ORDER BY U.due_date, \r\n" + 
			"         CASE \r\n" + 
			"            WHEN U.flow_type = 'FEE_EXP' THEN 1\r\n" + 
			"            WHEN U.flow_type = 'INTEREST_EXP' THEN 2\r\n" + 
			"            WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 3\r\n" + 
			"            WHEN U.flow_type = 'PENALTY_EXP' THEN 4\r\n" + 
			"         END\r\n" + 
			"", nativeQuery = true)
	List<Object[]> getloanflowsvaluedats(Date toDate, String accountNum);
	
	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = :accountNum AND DUE_DATE IN (:flowDates)", nativeQuery = true)
	List<LOAN_REPAYMENT_ENTITY> getLoanFlowsValueDatas1(@Param("accountNum") String accountNum, @Param("flowDates") List<String> flowDates);
	
	@Query(value = "SELECT * FROM LOAN_REPAYMENT_TBL WHERE PARENT_ACCOUNT_KEY = ?1 AND DUE_DATE =?2", nativeQuery = true)
	LOAN_REPAYMENT_ENTITY getLoanFlowsValueDatas11(String encodedkey, String flow_date);


	@Query(value = "SELECT\r\n" + //
				"    U.due_date,\r\n" + //
				"    CASE\r\n" + //
				"        WHEN U.flow_type = 'FEE_EXP' THEN '1'         -- Fees First\r\n" + //
				"        WHEN U.flow_type = 'INTEREST_EXP' THEN '2'    -- Interest Second\r\n" + //
				"        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN '3'   -- Principal Third\r\n" + //
				"        WHEN U.flow_type = 'PENALTY_EXP' THEN '4'     -- Penalty Last\r\n" + //
				"    END AS flow_id,\r\n" + //
				"    CASE\r\n" + //
				"        WHEN U.flow_type = 'FEE_EXP' THEN 'FEEDEM'\r\n" + //
				"        WHEN U.flow_type = 'INTEREST_EXP' THEN 'INDEM'\r\n" + //
				"        WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 'PRDEM'\r\n" + //
				"        WHEN U.flow_type = 'PENALTY_EXP' THEN 'PENALTY'\r\n" + //
				"    END AS flow_code,\r\n" + //
				"    U.flow_amt,\r\n" + //
				"    U.loan_acct_no,\r\n" + //
				"    U.acct_name,\r\n" + //
				"    U.encoded_key\r\n" + //
				"FROM (\r\n" + //
				"    SELECT\r\n" + //
				"        B.due_date,\r\n" + //
				"        A.ID AS loan_acct_no,\r\n" + //
				"        A.LOAN_NAME AS acct_name,\r\n" + //
				"        A.ENCODED_KEY,\r\n" + //
				"        -- Calculate remaining due amount\r\n" + //
				"        B.PRINCIPAL_EXP - B.PRINCIPAL_PAID AS PRINCIPAL_EXP,\r\n" + //
				"        B.INTEREST_EXP - B.INTEREST_PAID AS INTEREST_EXP,\r\n" + //
				"        B.FEE_EXP - B.FEE_PAID AS FEE_EXP,\r\n" + //
				"        B.PENALTY_EXP - B.PENALTY_PAID AS PENALTY_EXP\r\n" + //
				"    FROM LOAN_ACCOUNT_MASTER_TBL A\r\n" + //
				"    JOIN LOAN_REPAYMENT_TBL B\r\n" + //
				"        ON A.ENCODED_KEY = B.PARENT_ACCOUNT_KEY\r\n" + //
				"    WHERE\r\n" + //
				"         B.PARENT_ACCOUNT_KEY = ?1 \r\n" + //
				"        AND B.payment_state = 'PENDING'\r\n" + //
				") P\r\n" + //
				"UNPIVOT (\r\n" + //
				"    flow_amt FOR flow_type IN (PRINCIPAL_EXP, INTEREST_EXP, FEE_EXP, PENALTY_EXP)\r\n" + //
				") AS U\r\n" + //
				"-- Only show amounts where remaining due > 0\r\n" + //
				"WHERE U.flow_amt > 0\r\n" + //
				"ORDER BY U.due_date,\r\n" + //
				"         CASE\r\n" + //
				"            WHEN U.flow_type = 'FEE_EXP' THEN 1\r\n" + //
				"            WHEN U.flow_type = 'INTEREST_EXP' THEN 2\r\n" + //
				"            WHEN U.flow_type = 'PRINCIPAL_EXP' THEN 3\r\n" + //
				"            WHEN U.flow_type = 'PENALTY_EXP' THEN 4\r\n" + //
				"         END\r\n" + //
				"", nativeQuery = true)
	List<Object[]> getloanupdateList(String accountNum);


}
