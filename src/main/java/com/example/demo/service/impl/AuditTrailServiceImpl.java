package com.example.demo.service.impl;

import com.example.demo.entity.AuditTrailRecord;
import com.example.demo.repository.AuditTrailRecordRepository;
import com.example.demo.service.AuditTrailService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {
    private final AuditTrailRecordRepository repository;

    public AuditTrailServiceImpl(AuditTrailRecordRepository repository) {
        this.repository = repository;
    }

    public AuditTrailRecord logEvent(AuditTrailRecord record) {
        if (record.getLoggedAt() == null) {
            record.setLoggedAt(LocalDateTime.now());
        }
        return repository.save(record);
    }

    public List<AuditTrailRecord> getLogsByCredential(Long id) { return repository.findByCredentialId(id); }
}