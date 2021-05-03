package com.richotaru.notificationapi.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TwilioSmsSenderConfig {
    @Value("${TWILIO_ACCOUNT_SID:AC692c5fdf6366271fab91d81b78f52381}")
    private String ACCOUNT_SID;
    @Value("${TWILIO_AUTH_TOKEN:9f3dce41ad8a374ffd278daab8d61c31}")
    private String AUTH_TOKEN;
    @Value("${TWILIO_SENDER_NUMBER:+17135972411}")
    private String SENDER_NUMBER;
}
