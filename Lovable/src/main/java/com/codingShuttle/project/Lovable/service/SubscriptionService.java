package com.codingShuttle.project.Lovable.service;

import com.codingShuttle.project.Lovable.dto.subscription.CheckOutRequest;
import com.codingShuttle.project.Lovable.dto.subscription.CheckoutResponse;
import com.codingShuttle.project.Lovable.dto.subscription.PortalResponse;
import com.codingShuttle.project.Lovable.dto.subscription.SubscriptionResponse;
import com.codingShuttle.project.Lovable.enums.SubscriptionStatus;
import com.stripe.model.Subscription;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();


    void activateSubscription(Long userId, Long planId, String subcriptionId, String customerId);

    void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String id);

    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subId);

    boolean canCreateProject();
}
