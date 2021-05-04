package com.richotaru.notificationapi.configuration;


import com.richotaru.notificationapi.service.SettingService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Data
@Component
public class EmailConfig {
    @Autowired
    private SettingService settingService;
    public String EMAIL_NOTIFICATION_ADDRESS;
    public String EMAIL_NOTIFICATION_PASSWORD;
    public Integer SMTP_PORT;
    public String SMTP_HOST;

    @PostConstruct()
    public void init() {
        this.EMAIL_NOTIFICATION_ADDRESS = settingService.getString("EMAIL_NOTIFICATION_ADDRESS","nothing");
        this.EMAIL_NOTIFICATION_PASSWORD = settingService.getString("EMAIL_NOTIFICATION_PASSWORD","nothing");
        this.SMTP_PORT = settingService.getInteger("SMTP_PORT",587);
        this.SMTP_HOST = settingService.getString("TWILIO_SENDER_NUMBER","nothing");
    }
}
