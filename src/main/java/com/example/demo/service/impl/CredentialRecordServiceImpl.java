package com.example.demo.service.impl;

import com.example.demo.entity.CredentialRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CredentialRecordRepository;
import com.example.demo.service.CredentialRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CredentialRecordServiceImpl
        implements CredentialRecordService {

    private final CredentialRecordRepository repository;

    public CredentialRecordServiceImpl(
            CredentialRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public CredentialRecord createCredential(CredentialRecord record) {

        if (record.getExpiryDate() != null &&
                record.getExpiryDate().isBefore(LocalDate.now())) {
            record.setStatus("EXPIRED");
        } else if (record.getStatus() == null) {
            record.setStatus("VALID");
        }

        return repository.save(record);
    }

    @Override
    public CredentialRecord updateCredential(Long id,
                                             CredentialRecord updated) {

        CredentialRecord existing = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Credential not found"));

        existing.setCredentialCode(updated.getCredentialCode());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.setStatus(updated.getStatus());

        return repository.save(existing);
    }

    @Override
    public List<CredentialRecord> getCredentialsByHolder(Long holderId) {
        return repository.findByHolderId(holderId);
    }

    @Override
    public CredentialRecord getCredentialByCode(String code) {
        return repository.findByCredentialCode(code).orElse(null);
    }

    @Override
    public List<CredentialRecord> getAllCredentials() {
        return repository.findAll();
    }
}
