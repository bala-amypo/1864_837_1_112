package com.example.demo.service;
import com.example.demo.entity.CredentialHolderProfile;
public interface CredentialHolderProfileService {
    CredentialHolderProfile createHolder(CredentialHolderProfile profile);
    CredentialHolderProfile getHolderById(Long id);
    CredentialHolderProfile updateStatus(Long id, boolean active);
}