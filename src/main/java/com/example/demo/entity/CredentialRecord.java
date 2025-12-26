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

    private Long holderId; // Foreign key logic for testing

    @Column(unique = true)
    private String credentialCode;

    private String title;
    private String issuer;
    private String credentialType; // CERTIFICATE or LICENSE
    private String status; // VALID or EXPIRED
    private LocalDate expiryDate;

    @Column(columnDefinition = "TEXT")
    private String metadataJson; // Must start with {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "credential_rules",
        joinColumns = @JoinColumn(name = "credential_id"),
        inverseJoinColumns = @JoinColumn(name = "rule_id")
    )
    private Set<VerificationRule> rules = new HashSet<>();
}