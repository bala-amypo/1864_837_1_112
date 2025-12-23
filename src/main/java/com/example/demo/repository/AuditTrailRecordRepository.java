package com.example.demo.repository;

import com.example.demo.entity.AuditTrailRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditTrailRecordRepository extends JpaRepository<AuditTrailRecord, Long> {
    List<AuditTrailRecord> findByCredentialId(Long credentialId);
}