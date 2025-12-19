package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_requests")
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long credentialId;
    private String requestedBy;
    private String verificationMethod;
    private String status;
    private LocalDateTime verifiedAt;
    private String resultMessage;
}
