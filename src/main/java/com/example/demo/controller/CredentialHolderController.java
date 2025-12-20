package com.example.demo.controller;

import com.example.demo.entity.CredentialHolderProfile;
import com.example.demo.service.CredentialHolderProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holders")
@Tag(name = "Credential Holders")
public class CredentialHolderController {

    private final CredentialHolderProfileService service;

    public CredentialHolderController(
            CredentialHolderProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CredentialHolderProfile> create(
            @RequestBody CredentialHolderProfile profile) {
        return ResponseEntity.ok(service.createHolder(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredentialHolderProfile> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getHolderById(id));
    }

    @GetMapping
    public ResponseEntity<List<CredentialHolderProfile>> getAll() {
        return ResponseEntity.ok(service.getAllHolders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CredentialHolderProfile> updateStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(
                service.updateHolderStatus(id, active));
    }
}
