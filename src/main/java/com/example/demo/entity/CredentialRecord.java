package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class CredentialRecord {

    @Id
    @GeneratedValue
    private Long id;

    private Long holderId;

    @Column(unique = true)
    private String credentialCode;

    private String title;
    private String issuer;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String credentialType;
    private String status;
    private String metadataJson;

    @ManyToMany
    private Set<VerificationRule> rules;

    // getters and setters
}
