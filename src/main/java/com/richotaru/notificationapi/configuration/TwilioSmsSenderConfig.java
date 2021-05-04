package com.richotaru.notificationapi.configuration;


import com.richotaru.notificationapi.service.SettingService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Data
@Component
public class TwilioSmsSenderConfig {
    private String ACCOUNT_SID;
    private String AUTH_TOKEN;
    private String SENDER_NUMBER;
    @Autowired
    private SettingService settingService;

    @PostConstruct()
    public void init() {
        this.AUTH_TOKEN = settingService.getString("TWILIO_AUTH_TOKEN","nothing");
        this.ACCOUNT_SID = settingService.getString("TWILIO_ACCOUNT_SID","nothing");
        this.SENDER_NUMBER = settingService.getString("TWILIO_SENDER_NUMBER","nothing");
    }
}
