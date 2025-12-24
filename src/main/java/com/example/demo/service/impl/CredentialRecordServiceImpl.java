package com.example.demo.service.impl;

import com.example.demo.entity.CredentialRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CredentialRecordRepository;
import com.example.demo.service.CredentialRecordService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CredentialRecordServiceImpl implements CredentialRecordService {
    private final CredentialRecordRepository credentialRepo;

    public CredentialRecordServiceImpl(CredentialRecordRepository credentialRepo) {
        this.credentialRepo = credentialRepo;
    }

    @Override
    public CredentialRecord createCredential(CredentialRecord record) {
        // Requirement: Default status "VALID" if null
        if (record.getStatus() == null) {
            record.setStatus("VALID");
        }
        // Requirement: If expired, set status EXPIRED
        if (record.getExpiryDate() != null && record.getExpiryDate().isBefore(LocalDate.now())) {
            record.setStatus("EXPIRED");
        }
        return credentialRepo.save(record);
    }

    @Override
    public CredentialRecord updateCredential(Long id, CredentialRecord updated) {
        CredentialRecord existing = credentialRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
        // Copy updatable fields
        existing.setCredentialCode(updated.getCredentialCode());
        existing.setTitle(updated.getTitle());
        existing.setIssuer(updated.getIssuer());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.setMetadataJson(updated.getMetadataJson());
        return credentialRepo.save(existing);
    }

    @Override
    public List<CredentialRecord> getCredentialsByHolder(Long holderId) {
        return credentialRepo.findByHolderId(holderId);
    }

    @Override
    public CredentialRecord getCredentialByCode(String code) {
        return credentialRepo.findByCredentialCode(code).orElse(null);
    }

    @Override
    public List<CredentialRecord> getAllCredentials() {
        return credentialRepo.findAll();
    }
}