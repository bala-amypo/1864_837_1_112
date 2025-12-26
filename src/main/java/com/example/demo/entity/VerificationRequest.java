package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_requests")
public class VerificationRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long credentialId;
    private String status;
    private LocalDateTime verifiedAt;

    public VerificationRequest() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCredentialId() { return credentialId; }
    public void setCredentialId(Long c) { this.credentialId = c; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime v) { this.verifiedAt = v; }
}