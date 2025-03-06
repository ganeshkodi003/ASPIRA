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

}
