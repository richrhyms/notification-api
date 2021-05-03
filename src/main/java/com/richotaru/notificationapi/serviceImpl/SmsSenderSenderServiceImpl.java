package com.richotaru.notificationapi.serviceImpl;



import com.richotaru.notificationapi.configuration.TwilioSmsSenderConfig;
import com.richotaru.notificationapi.service.SmsSenderService;
import com.richotaru.notificationapi.enums.SmsProviderConstant;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SmsSenderSenderServiceImpl implements SmsSenderService {

    private final Environment environment;

    private final TwilioSmsSenderConfig twilioSmsSenderConfig;

    SmsProviderConstant SMS_PROVIDER;


    @Autowired
    public SmsSenderSenderServiceImpl(Environment environment, TwilioSmsSenderConfig twilioSmsSenderConfig) {
        this.environment = environment;
        this.twilioSmsSenderConfig = twilioSmsSenderConfig;
    }

    @PostConstruct
    public void init() {
        SMS_PROVIDER = SmsProviderConstant.valueOf(environment.getProperty("sms.provider", "TWILIO"));
        if (SMS_PROVIDER == SmsProviderConstant.TWILIO) {
            try {
                Twilio.init(twilioSmsSenderConfig.getACCOUNT_SID(), twilioSmsSenderConfig.getAUTH_TOKEN());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void sendSms(String message, List<String> recipients) {
        if (SMS_PROVIDER == SmsProviderConstant.TWILIO) {
            for (String recipient : recipients) {
                Message.creator(new PhoneNumber(recipient), // to
                        new PhoneNumber(twilioSmsSenderConfig.getSENDER_NUMBER()), // from
                        message)
                        .create();
            }
        }
    }

}

