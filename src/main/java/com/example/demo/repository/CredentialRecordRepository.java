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

    // Required for Test 27 and 58
    @Query("SELECT c FROM CredentialRecord c WHERE c.expiryDate < :date")
    List<CredentialRecord> findExpiredBefore(@Param("date") LocalDate date);

    // Required for Test 57 (Explicitly named HQL test)
    @Query("SELECT c FROM CredentialRecord c WHERE c.status = :status")
    List<CredentialRecord> findByStatusUsingHql(@Param("status") String status);

    // Required for Test 59 and 60
    List<CredentialRecord> searchByIssuerAndType(String issuer, String credentialType);
}