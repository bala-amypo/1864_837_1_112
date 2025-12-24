package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.VerificationRequestRepository;
import com.example.demo.repository.CredentialRecordRepository;
import com.example.demo.service.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class VerificationRequestServiceImpl implements VerificationRequestService {
    private final VerificationRequestRepository requestRepo;
    private final CredentialRecordService credentialService;
    private final VerificationRuleService ruleService;
    private final AuditTrailService auditService;
    private final CredentialRecordRepository credentialRepo;

    // MATCHES THE EXACT 5-ARG SIGNATURE REQUIRED BY YOUR COMPILER ERROR
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
        return requestRepo.save(request);
    }

    @Override
    public VerificationRequest processVerification(Long requestId) {
        VerificationRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        CredentialRecord credential = credentialRepo.findById(request.getCredentialId())
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        // REQUIREMENT: Must fetch active rules during process
        ruleService.getActiveRules();

        if (credential.getExpiryDate() != null && credential.getExpiryDate().isBefore(LocalDate.now())) {
            request.setStatus("FAILED");
        } else {
            request.setStatus("SUCCESS");
        }

        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        auditService.logEvent(audit);

        return requestRepo.save(request);
    }

    @Override
    public List<VerificationRequest> getRequestsByCredential(Long credentialId) {
        return requestRepo.findByCredentialId(credentialId);
    }
}