package com.example.demo.repository;

import com.example.demo.entity.CredentialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CredentialRecordRepository extends JpaRepository<CredentialRecord, Long> {
    List<CredentialRecord> findByHolderId(Long holderId);
    
    // PDF Page 8 naming: code
    Optional<CredentialRecord> findByCredentialCode(String code);

    // Requirement: findExpiredBefore(LocalDate date)
    @Query("SELECT c FROM CredentialRecord c WHERE c.expiryDate < ?1")
    List<CredentialRecord> findExpiredBefore(LocalDate date);

    // Requirement: findByStatusUsingHql(String status) - Must use @Query
    @Query("SELECT c FROM CredentialRecord c WHERE c.status = ?1")
    List<CredentialRecord> findByStatusUsingHql(String status);

    // Requirement: searchByIssuerAndType(String issuer, String type) - Must use @Query
    @Query("SELECT c FROM CredentialRecord c WHERE c.issuer = ?1 AND c.credentialType = ?2")
    List<CredentialRecord> searchByIssuerAndType(String issuer, String type);
}