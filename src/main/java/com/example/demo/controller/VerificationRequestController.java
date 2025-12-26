package com.example.demo.controller;

import com.example.demo.entity.VerificationRequest;
import com.example.demo.service.VerificationRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/verification")
@Tag(name = "Verification Request")
public class VerificationRequestController {

    private final VerificationRequestService verificationService;

    public VerificationRequestController(VerificationRequestService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping
    public ResponseEntity<VerificationRequest> initiate(@RequestBody VerificationRequest request) {
        return ResponseEntity.ok(verificationService.initiateVerification(request));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<VerificationRequest> process(@PathVariable Long id) {
        return ResponseEntity.ok(verificationService.processVerification(id));
    }

    @GetMapping("/credential/{credentialId}")
    public ResponseEntity<List<VerificationRequest>> getByCredential(@PathVariable Long credentialId) {
        return ResponseEntity.ok(verificationService.getRequestsByCredential(credentialId));
    }

    @GetMapping
    public ResponseEntity<List<VerificationRequest>> getAll() {
        return ResponseEntity.ok(verificationService.getAllRequests());
    }
}