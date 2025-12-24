package com.example.demo.controller;

import com.example.demo.entity.VerificationRule;
import com.example.demo.service.VerificationRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}