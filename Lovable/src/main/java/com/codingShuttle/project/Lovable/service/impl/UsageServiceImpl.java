package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.dto.usage.PlanLimitsResponse;
import com.codingShuttle.project.Lovable.dto.usage.UsageTodayResponse;
import com.codingShuttle.project.Lovable.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsage(Long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsLimits(Long userId) {
        return null;
    }
}
