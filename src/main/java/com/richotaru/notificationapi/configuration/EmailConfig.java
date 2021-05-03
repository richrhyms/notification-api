package com.richotaru.notificationapi.configuration;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EmailConfig {
    @Value("${EMAIL_NOTIFICATION_ADDRESS:postmaster@sandbox929d588bfe4c42fb9fc54a3b55b9e3fe.mailgun.org}")
    public String EMAIL_NOTIFICATION_ADDRESS;
    @Value("${EMAIL_NOTIFICATION_PASSWORD:91e2940fe97ee4e0c2832409f9a0a788-2a9a428a-742ee7e5}")
    public String EMAIL_NOTIFICATION_PASSWORD;
    @Value("${SMTP_PORT:587}")
    public Integer SMTP_PORT;
    @Value("${SMTP_HOST:smtp.mailgun.org}")
    public String SMTP_HOST;

}
