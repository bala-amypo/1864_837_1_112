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

    private final VerificationRuleService ruleService;

    public VerificationRuleController(VerificationRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<VerificationRule> create(@RequestBody VerificationRule rule) {
        return ResponseEntity.ok(ruleService.createRule(rule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VerificationRule> update(@PathVariable Long id, @RequestBody VerificationRule updated) {
        return ResponseEntity.ok(ruleService.updateRule(id, updated));
    }

    @GetMapping("/active")
    public ResponseEntity<List<VerificationRule>> getActive() {
        return ResponseEntity.ok(ruleService.getActiveRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VerificationRule> getById(@PathVariable Long id) {
        // Implementation for service.getRuleById would go here
        return ResponseEntity.ok(null); 
    }

    @GetMapping
    public ResponseEntity<List<VerificationRule>> getAll() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }
}