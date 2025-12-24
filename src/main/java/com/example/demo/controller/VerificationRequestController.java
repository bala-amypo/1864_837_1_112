package com.example.demo.controller;

import com.example.demo.entity.VerificationRequest;
import com.example.demo.service.VerificationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
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
}