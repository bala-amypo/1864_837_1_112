package com.example.demo.service;

import com.example.demo.entity.VerificationRequest;
import com.example.demo.entity.CredentialRecord;
import com.example.demo.entity.AuditTrailRecord;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Override
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
}
