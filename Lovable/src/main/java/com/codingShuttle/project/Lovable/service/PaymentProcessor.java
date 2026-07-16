package com.codingShuttle.project.Lovable.service;


import com.codingShuttle.project.Lovable.dto.subscription.CheckOutRequest;
import com.codingShuttle.project.Lovable.dto.subscription.CheckoutResponse;
import com.codingShuttle.project.Lovable.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PaymentProcessor {
    CheckoutResponse createCheckoutSessionUrl(CheckOutRequest request);

    PortalResponse openCustomerPortal();

    void handelWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
