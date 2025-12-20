package com.example.demo.controller;

import com.example.demo.entity.VerificationRule;
import com.example.demo.service.VerificationRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@Tag(name = "Verification Rules")
public class VerificationRuleController {

    private final VerificationRuleService service;

    public VerificationRuleController(
            VerificationRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VerificationRule> create(
            @RequestBody VerificationRule rule) {
        return ResponseEntity.ok(service.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VerificationRule> update(
            @PathVariable Long id,
            @RequestBody VerificationRule rule) {
        return ResponseEntity.ok(
                service.updateRule(id, rule));
    }

    @GetMapping("/active")
    public ResponseEntity<List<VerificationRule>> active() {
        return ResponseEntity.ok(service.getActiveRules());
    }

    @GetMapping
    public ResponseEntity<List<VerificationRule>> all() {
        return ResponseEntity.ok(service.getAllRules());
    }
}
