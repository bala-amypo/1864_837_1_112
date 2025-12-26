package com.example.demo.service.impl;

import com.example.demo.entity.CredentialRecord;
import com.example.demo.repository.CredentialRecordRepository;
import com.example.demo.service.CredentialRecordService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CredentialRecordServiceImpl implements CredentialRecordService {
    private final CredentialRecordRepository repo;
    public CredentialRecordServiceImpl(CredentialRecordRepository repo) { this.repo = repo; }

    @Override
    public CredentialRecord createCredential(CredentialRecord r) {
        if (r.getExpiryDate() != null && r.getExpiryDate().isBefore(LocalDate.now())) {
            r.setStatus("EXPIRED");
        } else if (r.getStatus() == null) {
            r.setStatus("VALID");
        }
        return repo.save(r);
    }

    @Override
    public List<CredentialRecord> getCredentialsByHolder(Long holderId) {
        // Required for the verification process logic above
        return repo.findAll(); 
    }
    
    // Implement other standard repository wrapper methods...
}