package com.example.demo.service.impl;

import com.example.demo.entity.CredentialHolderProfile;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CredentialHolderProfileRepository;
import com.example.demo.service.CredentialHolderProfileService;
import org.springframework.stereotype.Service;
import java.util.List;

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
                .orElseThrow(() -> new ResourceNotFoundException("Holder not found with id: " + id));
    }

    @Override
    public List<CredentialHolderProfile> getAllHolders() {
        return holderRepo.findAll();
    }

    @Override
    public CredentialHolderProfile updateHolderStatus(Long id, boolean active) {
        CredentialHolderProfile profile = getHolderById(id);
        profile.setActive(active);
        return holderRepo.save(profile);
    }
}