package com.example.demo.service;

import com.example.demo.entity.VerificationRequest;
import java.util.List;

public interface VerificationRequestService {

    VerificationRequest processVerification(Long requestId);

    // Add these methods if your controller calls them
    List<VerificationRequest> getRequestsByCredential(Long credentialId);

    List<VerificationRequest> getAllRequests();

    VerificationRequest initiateVerification(VerificationRequest request);
}

