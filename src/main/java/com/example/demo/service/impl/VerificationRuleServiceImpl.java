package com.example.demo.service.impl;

import com.example.demo.entity.VerificationRule;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.VerificationRuleRepository;
import com.example.demo.service.VerificationRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VerificationRuleServiceImpl
        implements VerificationRuleService {

    private final VerificationRuleRepository repository;

    public VerificationRuleServiceImpl(
            VerificationRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public VerificationRule createRule(VerificationRule rule) {
        return repository.save(rule);
    }

    @Override
    public VerificationRule updateRule(Long id,
                                       VerificationRule updatedRule) {

        VerificationRule rule = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Rule not found"));

        rule.setActive(updatedRule.isActive());
        return repository.save(rule);
    }

    @Override
    public List<VerificationRule> getActiveRules() {
        return repository.findByActiveTrue();
    }

    @Override
    public List<VerificationRule> getAllRules() {
        return repository.findAll();
    }
}
