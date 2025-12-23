package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class VerificationRule {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String ruleCode;

    private String description;

    private String appliesToType;

    private String validationExpression;

    private boolean active = true;

    // getters and setters
}
