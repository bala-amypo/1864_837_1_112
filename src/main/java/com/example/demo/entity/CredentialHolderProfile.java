package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "credential_holder_profiles",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "holderId"),
        @UniqueConstraint(columnNames = "email")
    }
)
public class CredentialHolderProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String holderId;
    private String fullName;
    private String email;
    private String organization;
    private Boolean active = true;

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
