package com.example.demo.service.impl;

import com.example.demo.entity.CredentialRecord;
import com.example.demo.entity.VerificationRule;
import com.example.demo.repository.VerificationRuleRepository;
import com.example.demo.service.VerificationRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationRuleServiceImpl implements VerificationRuleService {

    private final VerificationRuleRepository repository;

    public VerificationRuleServiceImpl(VerificationRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public VerificationRule createRule(VerificationRule rule) {
        return repository.save(rule);
    }

    @Override
    public VerificationRule updateRule(Long id, VerificationRule updatedRule) {
        VerificationRule existing = repository.findById(id).orElseThrow();
        existing.setName(updatedRule.getName());
        existing.setActive(updatedRule.isActive());
        return repository.save(existing);
    }

    @Override
    public List<VerificationRule> getActiveRules() {
        return repository.findByActive(true);
    }

    @Override
    public List<VerificationRule> getAllRules() {
        return repository.findAll();
    }

    // <-- Implement the new method
    @Override
    public boolean validateRules(CredentialRecord credential) {
        // Implement your actual rule validation logic
        // For now, return true as default
        return true;
    }
}
