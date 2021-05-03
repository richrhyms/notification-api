package com.richotaru.notificationapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/notification")
public class NotificationController {

    @PostMapping("email")
    public ResponseEntity<String> sendEmailNotification(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("sms")
    public ResponseEntity<String> sendSmsNotification(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
