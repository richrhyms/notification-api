package com.richotaru.notificationapi.service;

import com.richotaru.notificationapi.enums.*;

public interface SmsSenderService {
    void sendSms(String message, String from, SmsProviderConstant provider, String... recipients);
}
