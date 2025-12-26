package com.example.demo.controller;

import com.example.demo.entity.VerificationRequest;
import com.example.demo.service.VerificationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verifications")
public class VerificationRequestController {
    private final VerificationRequestService service;
    public VerificationRequestController(VerificationRequestService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<VerificationRequest> initiate(@RequestBody VerificationRequest request) {
        return ResponseEntity.ok(service.initiateVerification(request));
    }
}