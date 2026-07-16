package com.codingShuttle.project.Lovable.Mapper;

import com.codingShuttle.project.Lovable.dto.subscription.PlanResponse;
import com.codingShuttle.project.Lovable.dto.subscription.SubscriptionResponse;


import com.codingShuttle.project.Lovable.entity.Plan;
import com.codingShuttle.project.Lovable.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") //here we are specifying which framework we are using

public interface SubscriptionMapper {

SubscriptionResponse toSubscriptionResponse(Subscription subscription);
PlanResponse toPlanResponse(Plan plan);
}
