package com.richotaru.notificationapi.service;

import com.richotaru.notificationapi.enums.*;

import java.util.List;

public interface SmsSenderService {
    void sendSms(String message, List<String> recipients);
}
