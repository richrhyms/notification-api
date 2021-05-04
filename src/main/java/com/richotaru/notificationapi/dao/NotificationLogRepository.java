package com.richotaru.notificationapi.dao;

import com.richotaru.notificationapi.entity.NotificationRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationRequestLog, Long> {

    @Query("select rl from NotificationRequestLog rl" +
            " where lower(rl.clientName) = lower(?1)")
    List<NotificationRequestLog> findByClientName(String clientName);
}
