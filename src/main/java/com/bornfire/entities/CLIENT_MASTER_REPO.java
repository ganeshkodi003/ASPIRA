package com.bornfire.entities;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CLIENT_MASTER_REPO extends JpaRepository<CLIENT_MASTER_ENTITY, String> {
    
    @Query(value = "SELECT * FROM CLIENT_MASTER_TBL", nativeQuery = true)
    List<CLIENT_MASTER_ENTITY> getClientDet();
    
    @Query(value = "SELECT * FROM CLIENT_MASTER_TBL WHERE CUSTOMER_ID = ?1", nativeQuery = true)
    CLIENT_MASTER_ENTITY getClientView(String cust);
}
