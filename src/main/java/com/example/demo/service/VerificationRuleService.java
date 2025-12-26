package com.example.demo.service;

import com.example.demo.entity.VerificationRule;
import java.util.List;

public interface VerificationRuleService {
    VerificationRule createRule(VerificationRule rule);
    VerificationRule updateRule(Long id, VerificationRule updatedRule);
    List<VerificationRule> getActiveRules();
    List<VerificationRule> getAllRules();
}