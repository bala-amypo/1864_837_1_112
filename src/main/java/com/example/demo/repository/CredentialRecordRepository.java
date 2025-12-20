package com.example.demo.repository;

import com.example.demo.entity.CredentialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CredentialRecordRepository extends JpaRepository<CredentialRecord, Long> {

    // ✅ Matches entity field: expiryDate
    List<CredentialRecord> findByExpiryDateBefore(LocalDate date);

    // ✅ Required by project
    CredentialRecord findByCredentialCode(String credentialCode);

    // ✅ Required by project
    List<CredentialRecord> findByHolderId(Long holderId);

    // ✅ Required: must use @Query (HQL)
    @Query("SELECT c FROM CredentialRecord c WHERE c.status = :status")
    List<CredentialRecord> findByStatusUsingHql(@Param("status") String status);

    // ✅ Required: must use @Query
    @Query("SELECT c FROM CredentialRecord c WHERE c.issuer = :issuer AND c.credentialType = :type")
    List<CredentialRecord> searchByIssuerAndType(
            @Param("issuer") String issuer,
            @Param("type") String type
    );
}
