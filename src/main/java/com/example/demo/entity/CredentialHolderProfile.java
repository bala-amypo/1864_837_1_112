package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class CredentialHolderProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String organization;
    private Boolean active = true;
}