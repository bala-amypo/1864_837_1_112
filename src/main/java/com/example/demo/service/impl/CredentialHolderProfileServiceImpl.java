package com.example.demo.service.impl;

import com.example.demo.entity.CredentialHolderProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CredentialHolderProfileRepository;
import com.example.demo.service.CredentialHolderProfileService;
import org.springframework.stereotype.Service;

@Service
public class CredentialHolderProfileServiceImpl implements CredentialHolderProfileService {

    private final CredentialHolderProfileRepository holderRepo;

    // Constructor Injection (Requirement 6.1)
    public CredentialHolderProfileServiceImpl(CredentialHolderProfileRepository holderRepo) {
        this.holderRepo = holderRepo;
    }

    @Override
    public CredentialHolderProfile createHolder(CredentialHolderProfile profile) {
        // Requirement: must call holderRepo.save(profile) and return the saved entity
        return holderRepo.save(profile);
    }

    @Override
    public CredentialHolderProfile getHolderById(Long id) {
        // Requirement: must call holderRepo.findById(id) and throw ResourceNotFoundException
        return holderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential Holder Profile not found with id: " + id));
    }

    @Override
    public CredentialHolderProfile updateStatus(Long id, boolean active) {
        // Requirement: load existing, update active, save and return
        CredentialHolderProfile existingProfile = getHolderById(id);
        existingProfile.setActive(active);
        return holderRepo.save(existingProfile);
    }
}