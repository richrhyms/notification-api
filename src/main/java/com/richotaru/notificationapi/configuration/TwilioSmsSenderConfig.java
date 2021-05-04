package com.richotaru.notificationapi.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TwilioSmsSenderConfig {
    @Value("${TWILIO_ACCOUNT_SID:AC52bf59519e9c12e0c8b18ebf5005a397}")
    private String ACCOUNT_SID;
    @Value("${TWILIO_AUTH_TOKEN:}")
    private String AUTH_TOKEN;
    @Value("${TWILIO_SENDER_NUMBER:+17135972411}")
    private String SENDER_NUMBER;
}
