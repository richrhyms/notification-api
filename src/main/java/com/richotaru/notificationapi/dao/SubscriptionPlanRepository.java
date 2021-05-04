package com.richotaru.notificationapi.dao;

import com.richotaru.notificationapi.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    @Query("select sb from SubscriptionPlan sb" +
            " where lower(sb.name) = lower(?1)")
    Optional<SubscriptionPlan> findByName(String name);
}
