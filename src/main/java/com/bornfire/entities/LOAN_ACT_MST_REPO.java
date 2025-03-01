package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface LOAN_ACT_MST_REPO extends JpaRepository<LOAN_ACT_MST_ENTITY, String>{
	 @Query(value = "SELECT * FROM LOAN_ACCOUNT_MASTER_TBL", nativeQuery = true)
	 List<LOAN_ACT_MST_ENTITY> getLoanActDet();
}
