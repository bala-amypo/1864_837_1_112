package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.VerificationRequestRepository;
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

    public VerificationRequestServiceImpl(
            VerificationRequestRepository requestRepo, 
            CredentialRecordService credentialService, 
            VerificationRuleService ruleService, 
            AuditTrailService auditService) {
        this.requestRepo = requestRepo;
        this.credentialService = credentialService;
        this.ruleService = ruleService;
        this.auditService = auditService;
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

        // Robust Lookup: Try ID first, then Code
        if (request.getCredentialId() != null) {
            try {
                credential = credentialService.getById(request.getCredentialId());
            } catch (ResourceNotFoundException e) {
                // Fallback to code if ID lookup fails
            }
        }
        
        if (credential == null && request.getCredentialCode() != null) {
            credential = credentialService.getCredentialByCode(request.getCredentialCode());
        }

        if (credential == null) {
            throw new ResourceNotFoundException("Credential not found");
        }

        // Processing Logic
        boolean isExpired = credential.getExpiryDate() != null && 
                           credential.getExpiryDate().isBefore(LocalDate.now());

        if (isExpired || "EXPIRED".equals(credential.getStatus())) {
            request.setStatus("FAILED");
        } else {
            request.setStatus("SUCCESS");
        }

        // Log Audit Event
        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        audit.setEventTime(LocalDateTime.now());
        auditService.logEvent(audit);

        return requestRepo.save(request);
    }

    @Override
    public List<VerificationRequest> getRequestsByCredential(Long credentialId) {
        return requestRepo.findByCredentialId(credentialId);
    }
}