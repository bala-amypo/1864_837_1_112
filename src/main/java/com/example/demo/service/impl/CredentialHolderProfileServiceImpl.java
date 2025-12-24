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
    public CredentialHolderProfileServiceImpl(CredentialHolderProfileRepository repo) { this.holderRepo = repo; }

    @Override public CredentialHolderProfile createHolder(CredentialHolderProfile p) { return holderRepo.save(p); }
    @Override public CredentialHolderProfile getHolderById(Long id) { return holderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found")); }
    @Override public List<CredentialHolderProfile> getAllHolders() { return holderRepo.findAll(); }
    @Override public CredentialHolderProfile findByHolderId(String hid) { return holderRepo.findByHolderId(hid).orElseThrow(() -> new ResourceNotFoundException("Not found")); }
    
    @Override // Must match Interface name
    public CredentialHolderProfile updateHolderStatus(Long id, boolean active) {
        CredentialHolderProfile profile = getHolderById(id);
        profile.setActive(active);
        return holderRepo.save(profile);
    }
}