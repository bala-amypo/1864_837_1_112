package com.example.demo.service.impl;

import com.example.demo.entity.AuditTrailRecord;
import com.example.demo.repository.AuditTrailRecordRepository;
import com.example.demo.service.AuditTrailService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {

    private final AuditTrailRecordRepository auditRepo;

    public AuditTrailServiceImpl(AuditTrailRecordRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    @Override
    public AuditTrailRecord logEvent(AuditTrailRecord record) {
        // Requirement: If loggedAt is null, assign LocalDateTime.now()
        if (record.getLoggedAt() == null) {
            record.setLoggedAt(LocalDateTime.now());
        }
        return auditRepo.save(record);
    }

    @Override
    public List<AuditTrailRecord> getLogsByCredential(Long credentialId) {
        return auditRepo.findByCredentialId(credentialId);
    }

    @Override
    public List<AuditTrailRecord> getAllLogs() {
        return auditRepo.findAll();
    }
}