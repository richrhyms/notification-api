package com.richotaru.notificationapi.service;

import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;

public interface NotificationService {
    void sendSms(SmsRequestDto requestDto);
    void sendEmail(EmailRequestDto requestDto);
}
