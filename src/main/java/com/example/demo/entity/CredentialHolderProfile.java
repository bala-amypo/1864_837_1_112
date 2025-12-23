package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CredentialHolderProfile {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String holderId;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String organization;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters
}
