package com.example.demo.service.impl;

import com.example.demo.entity.CredentialHolderProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CredentialHolderProfileRepository;
import com.example.demo.service.CredentialHolderProfileService;
import org.springframework.stereotype.Service;

@Service
public class CredentialHolderProfileServiceImpl implements CredentialHolderProfileService {
    private final CredentialHolderProfileRepository holderRepo;

    public CredentialHolderProfileServiceImpl(CredentialHolderProfileRepository holderRepo) {
        this.holderRepo = holderRepo;
    }

    @Override
    public CredentialHolderProfile createHolder(CredentialHolderProfile profile) {
        return holderRepo.save(profile);
    }

    @Override
    public CredentialHolderProfile getHolderById(Long id) {
        return holderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holder not found"));
    }

    @Override
    public CredentialHolderProfile updateStatus(Long id, boolean active) {
        CredentialHolderProfile profile = getHolderById(id);
        profile.setActive(active);
        return holderRepo.save(profile);
    }
}