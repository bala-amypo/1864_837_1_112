package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class CredentialRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long holderId;
    @Column(unique = true)
    private String credentialCode;
    private String title;
    private String issuer;
    private String credentialType;
    private String status;
    private LocalDate expiryDate;
    private String metadataJson;

    @ManyToMany
    @JoinTable(name = "cred_rules", 
               joinColumns = @JoinColumn(name = "cred_id"),
               inverseJoinColumns = @JoinColumn(name = "rule_id"))
    private Set<VerificationRule> rules;
}