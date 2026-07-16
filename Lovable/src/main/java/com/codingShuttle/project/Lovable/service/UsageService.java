package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.usage.PlanLimitsResponse;
import com.codingShuttle.project.Lovable.dto.usage.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitsResponse getCurrentSubscriptionLimitsLimits(Long userId);
}
