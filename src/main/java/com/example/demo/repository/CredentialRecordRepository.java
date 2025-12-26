package com.example.demo.repository;

import com.example.demo.entity.CredentialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CredentialRecordRepository extends JpaRepository<CredentialRecord, Long> {
    
    List<CredentialRecord> findByHolderId(Long holderId);
    
    Optional<CredentialRecord> findByCredentialCode(String credentialCode);
    
    // Requirement: Find credentials where expiryDate is before a given date
    List<CredentialRecord> findExpiredBefore(LocalDate date);

    // Requirement: Must use HQL/JPQL via @Query
    @Query("SELECT c FROM CredentialRecord c WHERE c.status = :status")
    List<CredentialRecord> findByStatusUsingHql(@Param("status") String status);

    // Requirement: Must use HQL/JPQL via @Query
    @Query("SELECT c FROM CredentialRecord c WHERE c.issuer = :issuer AND c.credentialType = :credentialType")
    List<CredentialRecord> searchByIssuerAndType(@Param("issuer") String issuer, 
                                                 @Param("credentialType") String credentialType);
}