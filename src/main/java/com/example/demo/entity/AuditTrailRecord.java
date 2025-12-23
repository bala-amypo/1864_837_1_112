package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_trail_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditTrailRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long credentialId;

    private LocalDateTime loggedAt;
    
    // Ensure you have a PrePersist to handle the "if null set now" rule automatically if needed, 
    // though the requirement says logic should be in the ServiceImpl.
}