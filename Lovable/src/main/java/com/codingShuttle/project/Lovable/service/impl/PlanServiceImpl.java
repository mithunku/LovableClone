package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.dto.subscription.PlanResponse;
import com.codingShuttle.project.Lovable.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }
}
