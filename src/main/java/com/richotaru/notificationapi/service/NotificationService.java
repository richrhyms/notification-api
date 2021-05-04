package com.richotaru.notificationapi.service;

import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import com.richotaru.notificationapi.entity.NotificationRequestLog;

import java.util.List;

public interface NotificationService {
    void sendSms(SmsRequestDto requestDto);
    void sendEmail(EmailRequestDto requestDto);
    List<NotificationRequestLog> getNotificationLogs(String clientName);
}
