package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class CredentialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long holderId;
    private String credentialCode;
    private String title;
    private String issuer;
    private String credentialType; // Field name used in repository query
    private String status;
    private LocalDate expiryDate;
    private String metadataJson;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<VerificationRule> rules = new HashSet<>();
}