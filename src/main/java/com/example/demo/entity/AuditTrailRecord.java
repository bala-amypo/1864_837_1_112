package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trail_records")
public class AuditTrailRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long credentialId;
    private String eventType;
    private String details;
    private LocalDateTime loggedAt;

    @PrePersist
    void onLog() {
        this.loggedAt = LocalDateTime.now();
    }
}
