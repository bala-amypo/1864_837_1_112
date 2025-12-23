package com.example.demo.repository;

import com.example.demo.entity.VerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {
    List<VerificationRequest> findByCredentialId(Long credentialId);
}