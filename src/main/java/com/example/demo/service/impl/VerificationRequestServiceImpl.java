package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.VerificationRequestRepository;
import com.example.demo.service.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class VerificationRequestServiceImpl implements VerificationRequestService {

    private final VerificationRequestRepository verificationRequestRepo;
    private final CredentialRecordService credentialService;
    private final VerificationRuleService ruleService;
    private final AuditTrailService auditService;

    public VerificationRequestServiceImpl(VerificationRequestRepository vrRepo,
                                          CredentialRecordService crService,
                                          VerificationRuleService rService,
                                          AuditTrailService aService) {
        this.verificationRequestRepo = vrRepo;
        this.credentialService = crService;
        this.ruleService = rService;
        this.auditService = aService;
    }

    @Override
    public VerificationRequest initiateVerification(VerificationRequest request) {
        return verificationRequestRepo.save(request);
    }

    @Override
    public VerificationRequest processVerification(Long requestId) {
        // 1. Load request
        VerificationRequest request = verificationRequestRepo.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        // 2. Locate corresponding credential
        CredentialRecord credential = credentialService.getAllCredentials().stream()
                .filter(c -> c.getId().equals(request.getCredentialId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        // 3. Fetch active rules (required by logic rule)
        ruleService.getActiveRules();

        // 4. Expiry check logic
        if (credential.getExpiryDate() != null && credential.getExpiryDate().isBefore(LocalDate.now())) {
            request.setStatus("FAILED");
        } else {
            request.setStatus("SUCCESS");
        }

        // 5. Create Audit Record
        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        auditService.logEvent(audit);

        // 6. Save and return
        return verificationRequestRepo.save(request);
    }

    @Override
    public List<VerificationRequest> getRequestsByCredential(Long credentialId) {
        return verificationRequestRepo.findByCredentialId(credentialId);
    }

    @Override
    public List<VerificationRequest> getAllRequests() {
        return verificationRequestRepo.findAll();
    }
}