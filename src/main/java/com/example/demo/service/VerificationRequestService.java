package com.example.demo.service;
import com.example.demo.entity.VerificationRequest;
import java.util.List;

public interface VerificationRequestService {
    VerificationRequest initiateVerification(VerificationRequest request);
    VerificationRequest processVerification(Long requestId);
    List<VerificationRequest> getRequestsByCredential(Long credentialId);
    List<VerificationRequest> getAllRequests(); // REQUIRED
}