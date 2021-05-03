package com.richotaru.notificationapi.serviceImpl;

import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import com.richotaru.notificationapi.service.MailService;
import com.richotaru.notificationapi.service.NotificationService;
import com.richotaru.notificationapi.service.SmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private SmsSenderService smsSenderService;
    @Autowired
    private MailService mailService;
    @Override
    public void sendSms(SmsRequestDto requestDto) {

    }

    @Override
    public void sendEmail(EmailRequestDto requestDto) {

    }
}
