package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.subscription.PlanResponse;
import com.codingShuttle.project.Lovable.dto.subscription.SubscriptionResponse;
import com.codingShuttle.project.Lovable.entity.Plan;
import com.codingShuttle.project.Lovable.entity.Subscription;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-16T21:44:00+0530",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class SubscriptionMapperImpl implements SubscriptionMapper {

    @Override
    public SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        if ( subscription == null ) {
            return null;
        }

        PlanResponse plan = null;
        String status = null;
        Instant currentPeriodEnd = null;

        plan = toPlanResponse( subscription.getPlan() );
        if ( subscription.getStatus() != null ) {
            status = subscription.getStatus().name();
        }
        currentPeriodEnd = subscription.getCurrentPeriodEnd();

        Long tokenUsedThisCycle = null;

        SubscriptionResponse subscriptionResponse = new SubscriptionResponse( plan, status, currentPeriodEnd, tokenUsedThisCycle );

        return subscriptionResponse;
    }

    @Override
    public PlanResponse toPlanResponse(Plan plan) {
        if ( plan == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Integer maxProjects = null;
        Integer maxTokensPerDay = null;
        Integer maxPreviews = null;
        Boolean unlimitedAi = null;

        id = plan.getId();
        name = plan.getName();
        maxProjects = plan.getMaxProjects();
        maxTokensPerDay = plan.getMaxTokensPerDay();
        maxPreviews = plan.getMaxPreviews();
        unlimitedAi = plan.getUnlimitedAi();

        String price = null;

        PlanResponse planResponse = new PlanResponse( id, name, maxProjects, maxTokensPerDay, maxPreviews, unlimitedAi, price );

        return planResponse;
    }
}
