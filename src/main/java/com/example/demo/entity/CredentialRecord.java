package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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
    private String credentialType; 
    private String status; 
    private LocalDate expiryDate;

    @Column(columnDefinition = "TEXT")
    private String metadataJson;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "credential_rule_mapping",
        joinColumns = @JoinColumn(name = "credential_id"),
        inverseJoinColumns = @JoinColumn(name = "rule_id")
    )
    private List<VerificationRule> rules = new ArrayList<>(); 
}