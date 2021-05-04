package com.richotaru.notificationapi.serviceImpl;

import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import com.richotaru.notificationapi.service.MailService;
import com.richotaru.notificationapi.service.NotificationService;
import com.richotaru.notificationapi.service.SmsSenderService;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final SmsSenderService smsSenderService;
    private final MailService mailService;

    @Autowired
    public NotificationServiceImpl(MailService mailService, SmsSenderService smsSenderService) {
        this.mailService = mailService;
        this.smsSenderService = smsSenderService;
    }

    @Override
    public void sendSms(SmsRequestDto requestDto) {
        smsSenderService.sendSms(requestDto.getMessage(),requestDto.getRecipientPhoneNumbers());
    }

    @Override
    public void sendEmail(EmailRequestDto requestDto) {
       try {
           HtmlEmail htmlEmail = new HtmlEmail();
           htmlEmail.setSubject(requestDto.getSubject());
           htmlEmail.setMsg(requestDto.getMessage());
           htmlEmail.setTextMsg(requestDto.getMessage());
           htmlEmail.setFrom(requestDto.getSenderEmail(), requestDto.getSenderName());
           for (String email : requestDto.getRecipientEmails()) {
               htmlEmail.addTo(email);
           }
           mailService.send(htmlEmail);
       }catch (Exception e){
            e.printStackTrace();
       }

    }
}
