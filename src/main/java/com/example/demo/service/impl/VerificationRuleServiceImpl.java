package com.example.demo.service.impl;

import com.example.demo.entity.VerificationRule;
import com.example.demo.repository.VerificationRuleRepository;
import com.example.demo.service.VerificationRuleService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VerificationRuleServiceImpl implements VerificationRuleService {
    private final VerificationRuleRepository ruleRepo;

    public VerificationRuleServiceImpl(VerificationRuleRepository ruleRepo) {
        this.ruleRepo = ruleRepo;
    }

    @Override
    public VerificationRule createRule(VerificationRule rule) {
        return ruleRepo.save(rule);
    }

    @Override
    public List<VerificationRule> getActiveRules() {
        return ruleRepo.findByActiveTrue();
    }
}