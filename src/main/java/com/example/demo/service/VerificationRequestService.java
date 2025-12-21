package com.example.demo.service;

import com.example.demo.entity.VerificationRequest;
import com.example.demo.entity.CredentialRecord;
import com.example.demo.entity.AuditTrailRecord;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.VerificationRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationRequestService {

    private final VerificationRequestRepository repository;
    private final CredentialRecordService credentialRecordService;
    private final AuditTrailService auditTrailService;

    public VerificationRequestService(VerificationRequestRepository repository,
                                      CredentialRecordService credentialRecordService,
                                      AuditTrailService auditTrailService) {
        this.repository = repository;
        this.credentialRecordService = credentialRecordService;
        this.auditTrailService = auditTrailService;
    }

    // Process a verification request
    public VerificationRequest processVerification(Long requestId) {
        VerificationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        CredentialRecord credential =
                credentialRecordService.getCredentialById(request.getCredentialId());

        if (credential.getExpiryDate().isBefore(LocalDate.now())) {
            request.setStatus("FAILED");
            request.setResultMessage("Credential expired");
        } else {
            request.setStatus("SUCCESS");
            request.setResultMessage("Credential valid");
        }

        request.setVerifiedAt(LocalDateTime.now());

        AuditTrailRecord audit = new AuditTrailRecord();
        audit.setCredentialId(credential.getId());
        audit.setEventType("VERIFICATION");
        audit.setDetails(request.getStatus());

        auditTrailService.logEvent(audit);

        return repository.save(request);
    }

    // Get all requests for a specific credential
    public List<VerificationRequest> getRequestsByCredential(Long credentialId) {
        return repository.findByCredentialId(credentialId);
    }

    // Get all requests
    public List<VerificationRequest> getAllRequests() {
        return repository.findAll();
    }

    // Initiate a new verification request
    public VerificationRequest initiateVerification(VerificationRequest request) {
        return repository.save(request);
    }
}
