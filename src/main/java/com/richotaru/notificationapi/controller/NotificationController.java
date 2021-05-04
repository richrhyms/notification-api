package com.richotaru.notificationapi.controller;

import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import com.richotaru.notificationapi.enums.PricingPlan;
import com.richotaru.notificationapi.service.MailService;
import com.richotaru.notificationapi.service.NotificationService;
import com.richotaru.notificationapi.service.SmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("email")
    public ResponseEntity<String> sendEmailNotification(@Valid @RequestBody EmailRequestDto requestDto){
//        notificationService.sendEmail(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body("Email Sent");
    }

    @PostMapping("sms")
    public ResponseEntity<String> sendSmsNotification(@Valid @RequestBody SmsRequestDto requestDto){
//        notificationService.sendSms(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("SMS Sent");
    }
}
