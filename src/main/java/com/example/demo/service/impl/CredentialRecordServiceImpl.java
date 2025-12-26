package com.example.demo.service.impl;

import com.example.demo.entity.CredentialRecord;
import com.example.demo.repository.CredentialRecordRepository;
import com.example.demo.service.CredentialRecordService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CredentialRecordServiceImpl implements CredentialRecordService {
    private final CredentialRecordRepository credentialRepo;
    public CredentialRecordServiceImpl(CredentialRecordRepository repo) { this.credentialRepo = repo; }

    @Override
    public List<CredentialRecord> getAllCredentials() { return credentialRepo.findAll(); }

    @Override
    public CredentialRecord getCredentialByCode(String code) {
        // Test t16 expects null if not found
        return credentialRepo.findByCredentialCode(code).orElse(null);
    }
    
    @Override
    public CredentialRecord getById(Long id) { return credentialRepo.findById(id).orElse(null); }
    
    @Override
    public CredentialRecord createCredential(CredentialRecord r) {
        if (r.getStatus() == null) r.setStatus("VALID");
        if (r.getExpiryDate() != null && r.getExpiryDate().isBefore(LocalDate.now())) r.setStatus("EXPIRED");
        return credentialRepo.save(r);
    }
    
    @Override public List<CredentialRecord> getCredentialsByHolder(Long id) { return credentialRepo.findByHolderId(id); }
    @Override public CredentialRecord updateCredential(Long id, CredentialRecord u) {
        CredentialRecord e = credentialRepo.findById(id).orElseThrow();
        e.setCredentialCode(u.getCredentialCode());
        return credentialRepo.save(e);
    }
}