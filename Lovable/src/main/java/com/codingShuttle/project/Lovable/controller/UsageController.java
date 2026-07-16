package com.codingShuttle.project.Lovable.controller;

import com.codingShuttle.project.Lovable.dto.usage.PlanLimitsResponse;
import com.codingShuttle.project.Lovable.dto.usage.UsageTodayResponse;
import com.codingShuttle.project.Lovable.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage()
    {
        Long userId=1L;
        return ResponseEntity.ok(usageService.getTodayUsage(userId));
    }

    @GetMapping("/Limits")
    public ResponseEntity<PlanLimitsResponse> getPlanLimits()
    {
        Long userId=1L;
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimitsLimits(userId));
    }


}
