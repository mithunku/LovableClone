package com.codingShuttle.project.Lovable.service.impl;

import com.codingShuttle.project.Lovable.Mapper.SubscriptionMapper;
import com.codingShuttle.project.Lovable.Repository.PlanRepository;
import com.codingShuttle.project.Lovable.Repository.ProjectMemberRepository;
import com.codingShuttle.project.Lovable.Repository.SubscriptionRepository;
import com.codingShuttle.project.Lovable.Repository.UserRepository;

import com.codingShuttle.project.Lovable.dto.subscription.SubscriptionResponse;
import com.codingShuttle.project.Lovable.entity.Plan;
import com.codingShuttle.project.Lovable.entity.Subscription;
import com.codingShuttle.project.Lovable.entity.User;
import com.codingShuttle.project.Lovable.enums.SubscriptionStatus;
import com.codingShuttle.project.Lovable.error.ResourceNotFoundException;
import com.codingShuttle.project.Lovable.security.AuthUtil;
import com.codingShuttle.project.Lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final ProjectMemberRepository projectMemberRepository;

    private final SubscriptionMapper subscriptionMapper;
    @Override
    public SubscriptionResponse getCurrentSubscription() {

        Long userId=authUtil.getCurrentUserId();
        Subscription currentSubscription= subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(SubscriptionStatus.ACTIVE,SubscriptionStatus.PAST_DUE,SubscriptionStatus.TRIALING))
                .orElse(new Subscription());
        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    //this method is called when the payment checkout is made the this method is called so here subscription status will be incomplete
    //once the invoice paid event  occurs then status will become active
    @Override
    public void activateSubscription(Long userId, Long planId, String subcriptionId, String customerId) {

        boolean exists=subscriptionRepository.existsByStripeSubscriptionId(subcriptionId);
        if(exists) return;

        Plan plan=getPlan(planId);
        User user=getUser(userId);

        Subscription subscription=new Subscription().builder().plan(plan)
                .user(user)
                .stripeSubscriptionId(subcriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);


    }

    @Override
    public void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

        boolean subscriptionHasBeenUpadated=false;
        Subscription subscription=getSubscription(subscriptionId);
        Plan plan=getPlan(planId);
        if(status!=null && status!=subscription.getStatus())
        {
            subscription.setStatus(status);
            subscriptionHasBeenUpadated=true;
        }
        if(periodStart!=null && !periodStart.equals(subscription.getCurrentPeriodStart()))
        {
            subscription.setCurrentPeriodStart(periodStart);
            subscriptionHasBeenUpadated=true;
        }

        if(periodEnd!=null && !periodEnd.equals(subscription.getCurrentPeriodEnd()))
        {
            subscription.setCurrentPeriodEnd(periodEnd);
            subscriptionHasBeenUpadated=true;
        }

        if(cancelAtPeriodEnd!=null && cancelAtPeriodEnd!=subscription.getCancelAtPeriodEnd())
        {
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            subscriptionHasBeenUpadated=true;
        }

        if(planId!=null && planId!=subscription.getPlan().getId())
        {
            Plan newplan= getPlan(planId);
            subscription.setPlan(newplan);
            subscriptionHasBeenUpadated=true;
        }



        if(subscriptionHasBeenUpadated)
        {
            subscriptionRepository.save(subscription);
            log.debug("Subscription has been updated: {}",subscriptionId);
        }


    }

    @Override
    public void cancelSubscription(String id) {
        Subscription subscription=getSubscription(id);

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubcriptionId, Instant periodStart, Instant periodEnd) {

        Subscription subscription=getSubscription(gatewaySubcriptionId);
        Instant newStart=periodStart !=null ? periodStart:subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus()==SubscriptionStatus.PAST_DUE || subscription.getStatus()==SubscriptionStatus.INCOMPLETE)
        {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

        subscriptionRepository.save(subscription);
    }

    @Override
    public void markSubscriptionPastDue(String subId) {

        Subscription subscription = getSubscription(subId);

        if(subscription.getStatus()==SubscriptionStatus.PAST_DUE)
        {
            return;
        }
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
        //we  can also notify user
    }

    @Override
    public boolean canCreateProject() {
        Long userId=authUtil.getCurrentUserId();
       SubscriptionResponse currentSubscription=getCurrentSubscription();
       int countOfOwnedProjects= projectMemberRepository.countProjectOwnedByUser(userId);
       if(currentSubscription.plan()==null)
       {

           return countOfOwnedProjects<10;
       }

       return countOfOwnedProjects < currentSubscription.plan().maxProjects();


    }

    public User getUser(Long userId)
    {
        User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user",Long.toString(userId)));
        return user;
    }

    public Plan getPlan(Long planId)
    {
        Plan plan=planRepository.findById(planId).orElseThrow(()->new ResourceNotFoundException("planId", Long.toString(planId)));
        return plan;
    }

    public Subscription getSubscription(String gatewaySubscriptionId)
    {
        Subscription subscription=subscriptionRepository.findBystripeSubscriptionId(gatewaySubscriptionId).orElseThrow(()->new ResourceNotFoundException("Subscription",gatewaySubscriptionId));
        return subscription;

    }


}
