package com.codingShuttle.project.Lovable.controller;

import com.codingShuttle.project.Lovable.dto.subscription.*;
import com.codingShuttle.project.Lovable.service.PaymentProcessor;
import com.codingShuttle.project.Lovable.service.PlanService;
import com.codingShuttle.project.Lovable.service.SubscriptionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BillingController {
    private final SubscriptionService subscriptionService;
    private final PlanService planService;
    private final PaymentProcessor paymentProcessor;
    @Value("${stripe.webhook-secret}")
    public String webHookSecret;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans(){
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription()
    {
        Long userId=1L;
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription());
    }

    @PostMapping("/api/payment/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(@RequestBody CheckOutRequest request)//checkoutRequest contains plan the user has selected to buy
    {

        return ResponseEntity.ok(paymentProcessor.createCheckoutSessionUrl(request));
    }

    @PostMapping("/api/payment/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){

        return ResponseEntity.ok(paymentProcessor.openCustomerPortal());
    }
    @PostMapping("/webhooks/payment")
    public ResponseEntity<String> handlePaymentWebhooks(@RequestBody String payload,@RequestHeader("Stripe-Signature") String signHeader)
    {
        try{
            Event event= Webhook.constructEvent(payload, signHeader, webHookSecret);
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {

                try{
                    stripeObject=dataObjectDeserializer.deserializeUnsafe();
                    if(stripeObject==null)
                    {
                        return ResponseEntity.ok().build();
                    }
                }
                catch (Exception e)
                {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deserialization failed");
                }
                // Deserialization failed, probably due to an API version mismatch.
                // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                // instructions on how to handle this case, or return an error here.
            }
            Map<String,String> metadata=new HashMap<>();
            if(stripeObject instanceof Session session)
            {
                metadata=session.getMetadata();
            }

            paymentProcessor.handelWebhookEvent(event.getType(),stripeObject,metadata);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
