package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet; // Required for initialization

@Entity
@Table(name = "credential_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredentialRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long holderId;

    @Column(unique = true, nullable = false)
    private String credentialCode;

    private String title;

    private String issuer;

    private String credentialType; // CERTIFICATE or LICENSE

    private String status; // VALID or EXPIRED

    private LocalDate expiryDate;

    @Column(columnDefinition = "TEXT")
    private String metadataJson;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "credential_rule_mapping",
        joinColumns = @JoinColumn(name = "credential_id"),
        inverseJoinColumns = @JoinColumn(name = "rule_id")
    )
    // Initialize the collection to avoid NullPointerException
    private Set<VerificationRule> rules = new HashSet<>(); 
}