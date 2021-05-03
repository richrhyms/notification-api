package com.richotaru.notificationapi.serviceImpl;


import com.richotaru.notificationapi.configuration.EmailConfig;
import com.richotaru.notificationapi.service.MailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {


    @Autowired
    private EmailConfig emailConfig;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Async
    @Override
    public void send(Email email) {
//        String fromEmail = settingService.getString(EMAIL_SENDER_FROM_EMAIL, "noreply@dentaldoor.com");
//        String fromName = settingService.getString(EMAIL_SENDER_FROM_NAME, "DentalDoor");

        try {
            email.setHostName(emailConfig.SMTP_HOST);
            email.setSmtpPort(emailConfig.SMTP_PORT);
            email.setAuthenticator(new DefaultAuthenticator(emailConfig.EMAIL_NOTIFICATION_ADDRESS,
                    emailConfig.EMAIL_NOTIFICATION_PASSWORD));

//            email.setFrom(fromEmail, fromName);

            email.setSSLOnConnect(true);
            email.setStartTLSEnabled(true);
            email.send();
        } catch (EmailException ex) {
            throw new RuntimeException(ex);
        }
    }
}
