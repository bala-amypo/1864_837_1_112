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
        
        CredentialRecord credential = credentialService.getById(request.getCredentialId());

        // Logic: SUCCESS only if date is not past AND status is not "EXPIRED"
        boolean isDateExpired = credential.getExpiryDate() != null && 
                               credential.getExpiryDate().isBefore(LocalDate.now());
        boolean isStatusExpired = "EXPIRED".equalsIgnoreCase(credential.getStatus());

        if (isDateExpired || isStatusExpired) {
            request.setStatus("FAILED");
        } else {
            request.setStatus("SUCCESS");
        }

        // Audit Trail requirement - Using 'loggedAt' as verified by t30
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