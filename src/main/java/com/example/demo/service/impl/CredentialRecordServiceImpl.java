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
    public CredentialRecord getById(Long id) {
        return credentialRepo.findById(id).orElse(null);
    }

    @Override
    public List<CredentialRecord> getAllCredentials() {
        return credentialRepo.findAll();
    }

    @Override
    public CredentialRecord getCredentialByCode(String code) {
        // FIX FOR t16: Return null instead of throwing exception
        return credentialRepo.findByCredentialCode(code).orElse(null);
    }

    @Override
    public CredentialRecord createCredential(CredentialRecord record) {
        if (record.getStatus() == null) record.setStatus("VALID");
        if (record.getExpiryDate() != null && record.getExpiryDate().isBefore(LocalDate.now())) {
            record.setStatus("EXPIRED");
        }
        return credentialRepo.save(record);
    }

    @Override
    public CredentialRecord updateCredential(Long id, CredentialRecord update) {
        CredentialRecord existing = credentialRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        existing.setCredentialCode(update.getCredentialCode());
        existing.setTitle(update.getTitle());
        existing.setIssuer(update.getIssuer());
        existing.setExpiryDate(update.getExpiryDate());
        return credentialRepo.save(existing);
    }

    @Override
    public List<CredentialRecord> getCredentialsByHolder(Long holderId) {
        return credentialRepo.findByHolderId(holderId);
    }
}