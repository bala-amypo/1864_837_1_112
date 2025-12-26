package com.example.demo.service.impl;

import com.example.demo.entity.AuditTrailRecord;
import com.example.demo.repository.AuditTrailRecordRepository;
import com.example.demo.service.AuditTrailService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {
    private final AuditTrailRecordRepository repo;
    public AuditTrailServiceImpl(AuditTrailRecordRepository repo) { this.repo = repo; }

    @Override
    public AuditTrailRecord logEvent(AuditTrailRecord record) {
        if (record.getLoggedAt() == null) {
            record.setLoggedAt(LocalDateTime.now());
        }
        return repo.save(record);
    }

    @Override
    public List<AuditTrailRecord> getLogsByCredential(Long id) {
        return repo.findByCredentialId(id);
    }
}