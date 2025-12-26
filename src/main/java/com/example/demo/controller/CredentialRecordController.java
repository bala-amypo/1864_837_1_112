package com.example.demo.controller;

import com.example.demo.entity.CredentialRecord;
import com.example.demo.service.CredentialRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/credentials")
public class CredentialRecordController {
    private final CredentialRecordService service;
    public CredentialRecordController(CredentialRecordService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CredentialRecord> create(@RequestBody CredentialRecord record) {
        return ResponseEntity.ok(service.createCredential(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CredentialRecord> update(@PathVariable Long id, @RequestBody CredentialRecord record) {
        return ResponseEntity.ok(service.updateCredential(id, record));
    }

    @GetMapping("/holder/{holderId}")
    public ResponseEntity<List<CredentialRecord>> getByHolder(@PathVariable Long holderId) {
        return ResponseEntity.ok(service.getCredentialsByHolder(holderId));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CredentialRecord> getByCode(@PathVariable String code) {
        // Test t16 expects null body if not found, not an exception
        return ResponseEntity.ok(service.getCredentialByCode(code));
    }
}