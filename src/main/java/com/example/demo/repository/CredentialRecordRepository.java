package com.example.demo.repository;

import com.example.demo.entity.CredentialRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CredentialRecordRepository extends JpaRepository<CredentialRecord, Long> {

    List<CredentialRecord> findByHolderId(Long holderId);

    Optional<CredentialRecord> findByCredentialCode(String code);

    List<CredentialRecord> findExpiryDateBefore(LocalDate date);

    @Query("FROM CredentialRecord c WHERE c.status = :status")
    List<CredentialRecord> findByStatusUsingHql(@Param("status") String status);

    @Query("FROM CredentialRecord c WHERE c.issuer = :issuer AND c.credentialType = :type")
    List<CredentialRecord> searchByIssuerAndType(@Param("issuer") String issuer,
                                                 @Param("type") String type);
}
