package com.richotaru.notificationapi.controller;

import com.richotaru.notificationapi.dao.NotificationLogRepository;
import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import com.richotaru.notificationapi.entity.NotificationRequestLog;
import com.richotaru.notificationapi.enums.PricingPlan;
import com.richotaru.notificationapi.service.MailService;
import com.richotaru.notificationapi.service.NotificationService;
import com.richotaru.notificationapi.service.SmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;


    @GetMapping("{senderName}")
    public ResponseEntity<List<NotificationRequestLog>> getNotificationLogs(@PathVariable("senderName") String clientName){
        return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationLogs(clientName));
    }

    @PostMapping("email")
    public ResponseEntity<String> sendEmailNotification(@Valid @RequestBody EmailRequestDto requestDto){
        notificationService.sendEmail(requestDto);
    return ResponseEntity.status(HttpStatus.OK).body("Email Sent");
    }

    @PostMapping("sms")
    public ResponseEntity<String> sendSmsNotification(@Valid @RequestBody SmsRequestDto requestDto){
        notificationService.sendSms(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("SMS Sent");
    }
}
