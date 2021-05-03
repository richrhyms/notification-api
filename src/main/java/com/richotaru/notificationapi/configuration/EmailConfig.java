package com.richotaru.notificationapi.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EmailConfig {
    @Value("${EMAIL_NOTIFICATION_ADDRESS:info@irembo.com}")
    public String EMAIL_NOTIFICATION_ADDRESS;
    @Value("${EMAIL_NOTIFICATION_PASSWORD:API-KEY-de3f47db99fe65cf51ea8747effc3e5c}")
    public String EMAIL_NOTIFICATION_PASSWORD;
    @Value("${SMTP_PORT:587}")
    public Integer SMTP_PORT;
    @Value("${SMTP_HOST:API-KEY-de3f47db99fe65cf51ea8747effc3e5c}")
    public String SMTP_HOST;

}
