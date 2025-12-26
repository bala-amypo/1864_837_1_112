package com.example.demo.controller;

import com.example.demo.entity.CredentialHolderProfile;
import com.example.demo.service.CredentialHolderProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/holders")
@Tag(name = "Credential Holder")
public class CredentialHolderController {

    private final CredentialHolderProfileService holderService;

    public CredentialHolderController(CredentialHolderProfileService holderService) {
        this.holderService = holderService;
    }

    @PostMapping
    public ResponseEntity<CredentialHolderProfile> create(@RequestBody CredentialHolderProfile profile) {
        return ResponseEntity.ok(holderService.createHolder(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredentialHolderProfile> getById(@PathVariable Long id) {
        return ResponseEntity.ok(holderService.getHolderById(id));
    }

    @GetMapping
    public ResponseEntity<List<CredentialHolderProfile>> getAll() {
        return ResponseEntity.ok(holderService.getAllHolders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CredentialHolderProfile> updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        return ResponseEntity.ok(holderService.updateHolderStatus(id, active));
    }
}