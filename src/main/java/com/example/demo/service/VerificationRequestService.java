package com.example.demo.service;

import com.example.demo.entity.VerificationRequest;
import java.util.List;

public interface VerificationRequestService {

    VerificationRequest processVerification(Long requestId);

    VerificationRequest initiateVerification(VerificationRequest request);

    List<VerificationRequest> getRequestsByCredential(Long credentialId);

    List<VerificationRequest> getAllRequests();
}
