package com.example.demo.repository;

import com.example.demo.entity.CredentialHolderProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialHolderProfileRepository extends JpaRepository<CredentialHolderProfile, Long> {
}