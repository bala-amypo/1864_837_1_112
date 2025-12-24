package com.example.demo.repository;

import com.example.demo.entity.CredentialHolderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CredentialHolderProfileRepository extends JpaRepository<CredentialHolderProfile, Long> {
    // Required by CredentialHolderProfileService.findByHolderId(String holderId)
    Optional<CredentialHolderProfile> findByHolderId(String holderId);
}