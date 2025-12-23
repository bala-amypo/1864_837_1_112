package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.VerificationRequestService;
import com.example.demo.service.AuditTrailService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class VerificationRequestServiceImpl implements VerificationRequestService {
    private final VerificationRequestRepository requestRepo;
    private final CredentialRecordRepository credentialRepo;
    private final VerificationRuleRepository ruleRepo;
    private final AuditTrailService auditService;

    public VerificationRequestServiceImpl(VerificationRequestRepository requestRepo, 
                                         CredentialRecordRepository credentialRepo,
                                         VerificationRuleRepository ruleRepo,
                                         AuditTrailService auditService) {
        this.requestRepo = requestRepo;
        this.credentialRepo = credentialRepo;
        this.ruleRepo = ruleRepo;
        this.auditService = auditService;
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

        // Logic: Fetch active rules (per PDF requirement)
        ruleRepo.findByActiveTrue();

        // Logic: Check expiry
        if (credential.getExpiryDate() != null && credential.getExpiryDate().isBefore(LocalDate.now())) {
            request.setStatus("FAILED");
        } else {
            request.setStatus("SUCCESS");
        }

        // Logic: Log Audit event
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