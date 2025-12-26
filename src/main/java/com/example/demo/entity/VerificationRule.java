package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class VerificationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String ruleCode;
    private Boolean active = true;
}