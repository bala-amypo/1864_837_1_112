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
        return credentialRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
    }

    @Override
    public CredentialRecord getCredentialByCode(String code) {
        // FIX for t16: Return null instead of throwing exception.
        // This allows the controller/test to handle the 'not found' state as expected.
        return credentialRepo.findByCredentialCode(code).orElse(null);
    }

    @Override
    public CredentialRecord createCredential(CredentialRecord record) {
        if (record.getExpiryDate() != null && record.getExpiryDate().isBefore(LocalDate.now())) {
            record.setStatus("EXPIRED");
        } else if (record.getStatus() == null) {
            record.setStatus("VALID");
        }
        return credentialRepo.save(record);
    }

    @Override
    public CredentialRecord updateCredential(Long id, CredentialRecord update) {
        CredentialRecord existing = getById(id);
        existing.setCredentialCode(update.getCredentialCode());
        existing.setTitle(update.getTitle());
        existing.setIssuer(update.getIssuer());
        existing.setCredentialType(update.getCredentialType());
        existing.setStatus(update.getStatus());
        existing.setExpiryDate(update.getExpiryDate());
        existing.setMetadataJson(update.getMetadataJson());
        existing.setHolderId(update.getHolderId());
        // Ensure rules are also updated
        existing.setRules(update.getRules());
        return credentialRepo.save(existing);
    }

    @Override
    public List<CredentialRecord> getCredentialsByHolder(Long holderId) {
        return credentialRepo.findByHolderId(holderId);
    }
}