package com.example.demo.service.impl;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
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
    private final CredentialRecordRepository credentialRepo;

    public VerificationRequestServiceImpl(VerificationRequestRepository requestRepo, CredentialRecordService cs, 
                                         VerificationRuleService rs, AuditTrailService as, CredentialRecordRepository cr) {
        this.requestRepo = requestRepo; this.credentialService = cs; this.ruleService = rs; this.auditService = as; this.credentialRepo = cr;
    }

    @Override public VerificationRequest initiateVerification(VerificationRequest r) { return requestRepo.save(r); }
    @Override public List<VerificationRequest> getRequestsByCredential(Long id) { return requestRepo.findByCredentialId(id); }
    @Override public List<VerificationRequest> getAllRequests() { return requestRepo.findAll(); }

    @Override public VerificationRequest processVerification(Long requestId) {
        VerificationRequest request = requestRepo.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        CredentialRecord credential = credentialRepo.findById(request.getCredentialId()).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        ruleService.getActiveRules(); // Required interaction
        
        request.setStatus((credential.getExpiryDate() != null && credential.getExpiryDate().isBefore(LocalDate.now())) ? "FAILED" : "SUCCESS");
        request.setVerifiedAt(LocalDateTime.now());

        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        audit.setEventType("VERIFICATION");
        auditService.logEvent(audit);

        return requestRepo.save(request);
    }
}