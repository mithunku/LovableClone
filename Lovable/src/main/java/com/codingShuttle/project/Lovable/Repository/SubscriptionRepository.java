package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.Subscription;
import com.codingShuttle.project.Lovable.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> active);

    boolean existsByStripeSubscriptionId(String subcriptionId);

   Optional<Subscription> findBystripeSubscriptionId(String gatewaySubcriptionId);

    Optional<Subscription> findByUserId(Long userId);
}
