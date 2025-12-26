package com.example.demo.repository;

import com.example.demo.entity.CredentialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CredentialRecordRepository extends JpaRepository<CredentialRecord, Long> {
    List<CredentialRecord> findByHolderId(Long holderId);
    Optional<CredentialRecord> findByCredentialCode(String code);
    
    @Query("SELECT c FROM CredentialRecord c WHERE c.expiryDate < ?1")
    List<CredentialRecord> findExpiredBefore(LocalDate date);

    @Query("SELECT c FROM CredentialRecord c WHERE c.status = ?1")
    List<CredentialRecord> findByStatusUsingHql(String status);

    List<CredentialRecord> searchByIssuerAndType(String issuer, String type);
}