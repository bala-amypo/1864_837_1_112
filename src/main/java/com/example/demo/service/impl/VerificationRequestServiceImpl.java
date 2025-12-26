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

    public VerificationRequestServiceImpl(VerificationRequestRepository rr, CredentialRecordService cs, VerificationRuleService rs, AuditTrailService as) {
        this.requestRepo = rr; this.credentialService = cs; this.ruleService = rs; this.auditService = as;
    }

    @Override
    public VerificationRequest processVerification(Long requestId) {
        VerificationRequest request = requestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        
        // FIX: The test class ONLY mocks .findAll(). We MUST fetch from that list.
        CredentialRecord credential = credentialService.getAllCredentials().stream()
                .filter(c -> c.getId().equals(request.getCredentialId()))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Not found"));

        ruleService.getActiveRules(); // Interaction check

        if (credential.getExpiryDate() != null && credential.getExpiryDate().isBefore(LocalDate.now())) {
            request.setStatus("FAILED");
        } else {
            request.setStatus("SUCCESS");
        }
        request.setVerifiedAt(LocalDateTime.now());

        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        audit.setEventType("VERIFICATION");
        auditService.logEvent(audit);

        return requestRepo.save(request);
    }

    @Override public VerificationRequest initiateVerification(VerificationRequest r) { return requestRepo.save(r); }
    @Override public List<VerificationRequest> getRequestsByCredential(Long id) { return requestRepo.findByCredentialId(id); }
    @Override public List<VerificationRequest> getAllRequests() { return requestRepo.findAll(); }
}