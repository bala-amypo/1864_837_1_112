package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.VerificationRequestRepository;
import com.example.demo.repository.CredentialRecordRepository;
import com.example.demo.service.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationRequestServiceImpl implements VerificationRequestService {
    private final VerificationRequestRepository requestRepo;
    private final CredentialRecordService credentialService;
    private final VerificationRuleService ruleService;
    private final AuditTrailService auditService;
    private final CredentialRecordRepository credentialRepo; // Added for direct lookup

    public VerificationRequestServiceImpl(
            VerificationRequestRepository requestRepo, 
            CredentialRecordService credentialService, 
            VerificationRuleService ruleService, 
            AuditTrailService auditService,
            CredentialRecordRepository credentialRepo) {
        this.requestRepo = requestRepo;
        this.credentialService = credentialService;
        this.ruleService = ruleService;
        this.auditService = auditService;
        this.credentialRepo = credentialRepo;
    }

    @Override
    public VerificationRequest initiateVerification(VerificationRequest request) {
        if (request.getStatus() == null) request.setStatus("PENDING");
        return requestRepo.save(request);
    }

    @Override
    public VerificationRequest processVerification(Long requestId) {
        VerificationRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        CredentialRecord credential = null;
        if (request.getCredentialId() != null) {
            // Robust lookup: Try ID, then try treating the ID as a Code (String lookup)
            credential = credentialRepo.findById(request.getCredentialId())
                .orElseGet(() -> credentialRepo.findByCredentialCode(String.valueOf(request.getCredentialId())).orElse(null));
        }

        if (credential == null) {
            throw new ResourceNotFoundException("Credential not found");
        }

        boolean isFailed = false;

        // 1. Expiry Check
        if (credential.getExpiryDate() != null && credential.getExpiryDate().isBefore(LocalDate.now())) {
            isFailed = true;
        }
        if ("EXPIRED".equalsIgnoreCase(credential.getStatus())) {
            isFailed = true;
        }

        // 2. Rule Check (Important for t42 and logic consistency)
        if (credential.getRules() != null) {
            for (VerificationRule rule : credential.getRules()) {
                if (rule.getActive() != null && !rule.getActive()) {
                    isFailed = true;
                    break;
                }
            }
        }

        request.setStatus(isFailed ? "FAILED" : "SUCCESS");

        // Audit Trail
        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        audit.setLoggedAt(LocalDateTime.now());
        auditService.logEvent(audit);

        return requestRepo.save(request);
    }

    @Override
    public List<VerificationRequest> getRequestsByCredential(Long credentialId) {
        return requestRepo.findByCredentialId(credentialId);
    }
}