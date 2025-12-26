package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "credential_records")
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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<VerificationRule> rules = new HashSet<>();

    public CredentialRecord() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCredentialCode() { return credentialCode; }
    public void setCredentialCode(String c) { this.credentialCode = c; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String i) { this.issuer = i; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate d) { this.expiryDate = d; }
    public Long getHolderId() { return holderId; }
    public void setHolderId(Long h) { this.holderId = h; }
    public String getCredentialType() { return credentialType; }
    public void setCredentialType(String ct) { this.credentialType = ct; }
    public String getMetadataJson() { return metadataJson; }
    public void setMetadataJson(String m) { this.metadataJson = m; }
    public Set<VerificationRule> getRules() { return rules; }
    public void setRules(Set<VerificationRule> r) { this.rules = r; }
}