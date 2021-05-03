package com.richotaru.notificationapi.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TwilioSmsSenderConfig {
    @Value("${TWILIO_ACCOUNT_SID:AC4d614afb146bed570438889fa542208c}")
    private String ACCOUNT_SID;
    @Value("${TWILIO_AUTH_TOKEN:AC4d6 }")
    private String AUTH_TOKEN;
    @Value("${TWILIO_SENDER_NUMBER:+2347089786721}")
    private String SENDER_NUMBER;
}
