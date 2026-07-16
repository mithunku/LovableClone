package com.codingShuttle.project.Lovable.Repository;

import com.codingShuttle.project.Lovable.entity.Plan;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
   Optional<Plan> findByStripePriceId(String id);
}
