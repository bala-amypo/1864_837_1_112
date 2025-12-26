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
        // Requirement: If expiryDate is before today, set status to EXPIRED
        if (record.getExpiryDate() != null && record.getExpiryDate().isBefore(LocalDate.now())) {
            record.setStatus("EXPIRED");
        } else if (record.getStatus() == null) {
            // Requirement: If status is null and not expired, default to VALID
            record.setStatus("VALID");
        }
        return credentialRepo.save(record);
    }

    @Override
    public CredentialRecord updateCredential(Long id, CredentialRecord updated) {
        CredentialRecord existing = credentialRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
        
        if (updated.getCredentialCode() != null) {
            existing.setCredentialCode(updated.getCredentialCode());
        }
        // Update other updatable fields as necessary
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