package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Repository.PlanRepository;
import com.codingShuttle.project.Lovable.Repository.UserRepository;
import com.codingShuttle.project.Lovable.dto.subscription.CheckOutRequest;
import com.codingShuttle.project.Lovable.dto.subscription.CheckoutResponse;
import com.codingShuttle.project.Lovable.dto.subscription.PortalResponse;
import com.codingShuttle.project.Lovable.entity.Plan;
import com.codingShuttle.project.Lovable.entity.User;
import com.codingShuttle.project.Lovable.enums.SubscriptionStatus;
import com.codingShuttle.project.Lovable.error.BadRequestException;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.PaymentProcessor;
import com.codingShuttle.project.Lovable.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessorImpl implements PaymentProcessor {

    private final AuthUtil authUtil;
    private final PlanRepository planRepository;
    private final UserRepository userRepo;
    private final SubscriptionService subscriptionService;
    @Value("${domain.url}")
    private  String domainUrl;
    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckOutRequest request) {
        System.out.println("entered cehckout srvice layer");
        Plan plan=planRepository.findById(request.planId())
                .orElseThrow(()->new ResourceNotFoundException("Plan",request.planId().toString()));

        System.out.println("plan: "+plan.getId());
        Long userId=authUtil.getCurrentUserId();
        User user=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("USER",userId.toString()));

        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripePriceId())
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)

                .setSuccessUrl(domainUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(domainUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());


        try{
            if(user.getStripeCustomerId()==null || user.getStripeCustomerId().isBlank()==true)
            {
                params.setCustomerEmail(user.getUsername());
            }
            else {
                params.setCustomer(user.getStripeCustomerId());
            }
            Session session = Session.create(params.build());
            System.out.println("session object" + session);
            System.out.println("Sesiion url" + session.getUrl());
            return new CheckoutResponse(session.getUrl());
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }


    }

    @Override
    public PortalResponse openCustomerPortal() {
        Long userId=authUtil.getCurrentUserId();
        User user=getUser(userId);
        String stripeCustomerId=user.getStripeCustomerId();
        if(stripeCustomerId==null || stripeCustomerId.isEmpty())
        {
            throw new BadRequestException("User does not have a Stripe Customer Id , UserId:" + userId);
        }

        try
        {
            var portalSession= com.stripe.model.billingportal.Session.create(
                    com.stripe.param.billingportal.SessionCreateParams.builder()
                            .setCustomer(stripeCustomerId)
                            .setReturnUrl(domainUrl)
                            .build()
            );
            return new PortalResponse(portalSession.getUrl());
        }
        catch (StripeException e)
        {
            throw new RuntimeException(e);
        }



    }

    @Override
    public void handelWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata) {

        switch (type)
        {
            case "checkout.session.completed": handleCheckoutSessionCompleted((Session) stripeObject,metadata);
                                                break;
            case  "customer.subscription.updated": handleCustomerSubscriptionUpdated((Subscription) stripeObject);
                break;
            case "customer.subscription.deleted" : handleCustomerSubscriptionDeleted((Subscription) stripeObject);
                break;
            case "invoice.paid": handleInvoicePaid((Invoice) stripeObject);
                break;
            case "invoice.payment_failed": handleInvoicePaymentFailed((Invoice) stripeObject);
                break;//update status pastdue
            default: log.debug("Igoring the event : {}",type);
        }
    }

    private void handleInvoicePaid(Invoice invoice) {
        String subId=extractSubscriptionId(invoice);

        if(subId==null)
        {
            return;
        }

        try{
        Subscription subscription=Subscription.retrieve(subId);
        var item=subscription.getItems().getData().get(0);
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd=toInstant(item.getCurrentPeriodEnd());

        subscriptionService.renewSubscriptionPeriod(subId,periodStart,periodEnd);
        }
        catch (StripeException e)
        {
            throw new RuntimeException(e);
        }

    }

    private void handleInvoicePaymentFailed(Invoice invoice)
    {
        String subId=extractSubscriptionId(invoice);

        if(subId==null)
        {
            return;
        }

        subscriptionService.markSubscriptionPastDue(subId);
    }

    private String extractSubscriptionId(Invoice invoice) {

        var parent=invoice.getParent();
        if(parent==null) return null;

        var subDetails=parent.getSubscriptionDetails();
        if(subDetails==null) return null;

        return subDetails.getSubscription();
    }

    private void handleCustomerSubscriptionDeleted(Subscription subscription) {
        if(subscription==null)
        {
            log.info("subscription object is null");
            return;
        }

        subscriptionService.cancelSubscription(subscription.getId());
    }

    private void handleCustomerSubscriptionUpdated(Subscription subscription) {
        if(subscription==null)
        {
            log.info("subscription object is null");
            return;
        }
        SubscriptionItem item=subscription.getItems().getData().get(0);
        SubscriptionStatus status=mapStripeStatusToEnum(subscription.getStatus());
        Instant periodStart=toInstant(item.getCurrentPeriodStart());
        Instant periodEnd=toInstant(item.getCurrentPeriodEnd());

        Long planId=resolvePlanId(item.getPrice());

        subscriptionService.updateSubscription(subscription.getId(),status,periodStart,periodEnd,subscription.getCancelAtPeriodEnd(), planId);


    }

    private Long resolvePlanId(Price price) {
        if(price==null || price.getId()==null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private SubscriptionStatus mapStripeStatusToEnum(String stripeStatus) {

        if (stripeStatus == null) {
            throw new IllegalArgumentException("Stripe status cannot be null");
        }

        switch (stripeStatus.toLowerCase()) {

            case "active":
                return SubscriptionStatus.ACTIVE;

            case "trialing":
                return SubscriptionStatus.TRIALING;

            case "canceled":
                return SubscriptionStatus.CANCELED;

            case "past_due":
                return SubscriptionStatus.PAST_DUE;

            case "incomplete":
                return SubscriptionStatus.INCOMPLETE;

            default:
                throw new IllegalArgumentException("Unknown Stripe status: " + stripeStatus);
        }
    }

    private Instant toInstant(Long epochSeconds) {
        if (epochSeconds == null) {
            return null; // or throw exception based on your design
        }
        return Instant.ofEpochSecond(epochSeconds);
    }
    public void handleCheckoutSessionCompleted(Session session,Map<String,String> metadata)
    {
        if(session==null)
        {
            log.info("session is null");
            return;
        }
        Long userId=Long.parseLong(metadata.get("user_id"));
        Long planId=Long.parseLong(metadata.get("plan_id"));
        User user=getUser(userId);
        String customerId=session.getCustomer();
        String subcriptionId=session.getSubscription();
        if(user.getStripeCustomerId()==null)
        {
            user.setStripeCustomerId(customerId);
            userRepo.save(user);
        }


        subscriptionService.activateSubscription(userId,planId,subcriptionId,customerId);
    }


    public User getUser(Long userId)
    {
        User user=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("USER",userId.toString()));
        return user;

    }
}
