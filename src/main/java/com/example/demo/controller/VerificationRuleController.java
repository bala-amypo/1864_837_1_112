package com.example.demo.controller;

import com.example.demo.entity.VerificationRule;
import com.example.demo.service.VerificationRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class VerificationRuleController {

    private final VerificationRuleService ruleService;

    public VerificationRuleController(VerificationRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<VerificationRule> create(@RequestBody VerificationRule rule) {
        return ResponseEntity.ok(ruleService.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VerificationRule> update(@PathVariable Long id, @RequestBody VerificationRule updatedRule) {
        return ResponseEntity.ok(ruleService.updateRule(id, updatedRule));
    }

    @GetMapping("/active")
    public ResponseEntity<List<VerificationRule>> getActive() {
        return ResponseEntity.ok(ruleService.getActiveRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerificationRule> getById(@PathVariable Long id) {
        // We will add this helper to the service next
        return ResponseEntity.ok(ruleService.getAllRules().stream()
                .filter(r -> r.getId().equals(id)).findFirst().orElse(null));
    }

    @GetMapping
    public ResponseEntity<List<VerificationRule>> getAll() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }
}