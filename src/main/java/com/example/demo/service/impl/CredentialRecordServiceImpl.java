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
    public List<CredentialRecord> findAll() { return repo.findAll(); }

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
    public CredentialRecord updateCredential(Long id, CredentialRecord update) {
        CredentialRecord existing = repo.findById(id).orElseThrow();
        existing.setCredentialCode(update.getCredentialCode());
        return repo.save(existing);
    }
    @Override
    public List<CredentialRecord> getCredentialsByHolder(Long holderId) { return repo.findByHolderId(holderId); }
    @Override
    public CredentialRecord getCredentialByCode(String code) { return repo.findByCredentialCode(code).orElse(null); }
}