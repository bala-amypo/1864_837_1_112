package com.example.demo.controller;

import com.example.demo.entity.AuditTrailRecord;
import com.example.demo.service.AuditTrailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit Trail")
public class AuditTrailController {

    private final AuditTrailService auditService;

    public AuditTrailController(AuditTrailService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    public ResponseEntity<AuditTrailRecord> log(@RequestBody AuditTrailRecord record) {
        return ResponseEntity.ok(auditService.logEvent(record));
    }

    @GetMapping("/credential/{credentialId}")
    public ResponseEntity<List<AuditTrailRecord>> getByCredential(@PathVariable Long credentialId) {
        return ResponseEntity.ok(auditService.getLogsByCredential(credentialId));
    }

    @GetMapping
    public ResponseEntity<List<AuditTrailRecord>> getAll() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }
}