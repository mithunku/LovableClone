package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.subscription.PlanResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanService {
    List<PlanResponse> getAllActivePlans();
}
