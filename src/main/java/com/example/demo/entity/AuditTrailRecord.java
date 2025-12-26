package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trail_records")
public class AuditTrailRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long credentialId;
    private String eventType;
    private LocalDateTime loggedAt;

    public AuditTrailRecord() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCredentialId() { return credentialId; }
    public void setCredentialId(Long c) { this.credentialId = c; }
    public String getEventType() { return eventType; }
    public void setEventType(String e) { this.eventType = e; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime l) { this.loggedAt = l; }
}