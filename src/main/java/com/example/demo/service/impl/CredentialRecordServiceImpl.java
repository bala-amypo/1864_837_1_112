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
    private final CredentialRecordRepository repo;
    public CredentialRecordServiceImpl(CredentialRecordRepository repo) { this.repo = repo; }

    @Override public CredentialRecord getById(Long id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found")); }
    @Override public List<CredentialRecord> getAllCredentials() { return repo.findAll(); }
    @Override public List<CredentialRecord> getCredentialsByHolder(Long hid) { return repo.findByHolderId(hid); }
    @Override public CredentialRecord getCredentialByCode(String code) { return repo.findByCredentialCode(code).orElse(null); }

    @Override public CredentialRecord createCredential(CredentialRecord r) {
        if (r.getStatus() == null) r.setStatus("VALID");
        if (r.getExpiryDate() != null && r.getExpiryDate().isBefore(LocalDate.now())) r.setStatus("EXPIRED");
        return repo.save(r);
    }

    @Override public CredentialRecord updateCredential(Long id, CredentialRecord u) {
        CredentialRecord e = getById(id);
        e.setCredentialCode(u.getCredentialCode());
        e.setTitle(u.getTitle());
        e.setIssuer(u.getIssuer());
        e.setExpiryDate(u.getExpiryDate());
        return repo.save(e);
    }
}