package com.richotaru.notificationapi.serviceImpl;


import com.richotaru.notificationapi.configuration.EmailConfig;
import com.richotaru.notificationapi.service.MailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImpl implements MailService {

    private final EmailConfig emailConfig;

    @Autowired
    public MailServiceImpl(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @Async
    @Override
    public void send(Email email) {
        try {
            email.setHostName(emailConfig.SMTP_HOST);
            email.setSmtpPort(emailConfig.SMTP_PORT);
            email.setAuthenticator(new DefaultAuthenticator(emailConfig.EMAIL_NOTIFICATION_ADDRESS,
                    emailConfig.EMAIL_NOTIFICATION_PASSWORD));
            email.setSSLOnConnect(true);
            email.setStartTLSEnabled(true);
            email.send();
        } catch (EmailException ex) {
//            throw new RuntimeException(ex);
        }
    }
}
