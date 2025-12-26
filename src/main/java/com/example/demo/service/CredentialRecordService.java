package com.example.demo.service;

import com.example.demo.entity.CredentialRecord;
import java.util.List;

public interface CredentialRecordService {
    CredentialRecord createCredential(CredentialRecord record);
    CredentialRecord updateCredential(Long id, CredentialRecord updated);
    List<CredentialRecord> getCredentialsByHolder(Long holderId);
    CredentialRecord getCredentialByCode(String code);
    List<CredentialRecord> getAllCredentials();
}