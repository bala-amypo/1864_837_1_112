package com.example.demo.service.impl;

import com.example.demo.entity.CredentialRecord;
import com.example.demo.repository.CredentialRecordRepository;
import com.example.demo.service.CredentialRecordService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CredentialRecordServiceImpl implements CredentialRecordService {
    private final CredentialRecordRepository repository;

    public CredentialRecordServiceImpl(CredentialRecordRepository repository) {
        this.repository = repository;
    }

    public CredentialRecord createCredential(CredentialRecord record) {
        if (record.getExpiryDate() != null && record.getExpiryDate().isBefore(LocalDate.now())) {
            record.setStatus("EXPIRED");
        } else if (record.getStatus() == null) {
            record.setStatus("VALID");
        }
        return repository.save(record);
    }

    public CredentialRecord updateCredential(Long id, CredentialRecord update) {
        CredentialRecord existing = repository.findById(id).orElseThrow();
        existing.setCredentialCode(update.getCredentialCode());
        // Copy other fields as per rules...
        return repository.save(existing);
    }

    public List<CredentialRecord> getCredentialsByHolder(Long holderId) { return repository.findByHolderId(holderId); }
    public CredentialRecord getCredentialByCode(String code) { return repository.findByCredentialCode(code).orElse(null); }
}