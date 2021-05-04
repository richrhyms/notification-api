package com.richotaru.notificationapi.serviceImpl;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.richotaru.notificationapi.enumeration.GenericStatusConstant;
import com.richotaru.notificationapi.enumeration.MessageDeliveryChannelConstant;
import com.richotaru.notificationapi.enumeration.MessagePriorityConstant;

import com.richotaru.notificationapi.dao.NotificationLogRepository;
import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import com.richotaru.notificationapi.entity.NotificationRequestLog;
import com.richotaru.notificationapi.service.MailService;
import com.richotaru.notificationapi.service.NotificationService;
import com.richotaru.notificationapi.service.SmsSenderService;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final SmsSenderService smsSenderService;
    private final MailService mailService;
    private final NotificationLogRepository notificationLogRepository;
    @Autowired
    public NotificationServiceImpl(MailService mailService, SmsSenderService smsSenderService,
                                   NotificationLogRepository notificationLogRepository) {
        this.mailService = mailService;
        this.smsSenderService = smsSenderService;
        this.notificationLogRepository = notificationLogRepository;
    }

    @Override
    public void sendSms(SmsRequestDto requestDto) {
        try{
            smsSenderService.sendSms(requestDto.getMessage(),requestDto.getRecipientPhoneNumbers());
            NotificationRequestLog log = new NotificationRequestLog();
            log.setPriority(MessagePriorityConstant.MID);
            log.setNotificationSent(false);
            log.setPayload(new Gson().toJson(requestDto));
            log.setMessageDeliveryChannel(MessageDeliveryChannelConstant.SMS);
            log.setRetryCount(5L);
            log.setStatus(GenericStatusConstant.ACTIVE);
            log.setDateDeactivated(LocalDateTime.now());
            log.setCreatedAt(LocalDateTime.now());
            log.setLastUpdatedAt(LocalDateTime.now());
            notificationLogRepository.save(log);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmail(EmailRequestDto requestDto) {
        NotificationRequestLog log = new NotificationRequestLog();
        log.setClientName(requestDto.getSenderName());
        log.setPriority(MessagePriorityConstant.LOW);
        log.setNotificationSent(false);
        log.setPayload(new Gson().toJson(requestDto));
        log.setMessageDeliveryChannel(MessageDeliveryChannelConstant.EMAIL);
        log.setRetryCount(5L);
        log.setStatus(GenericStatusConstant.ACTIVE);
        log.setDateDeactivated(LocalDateTime.now());
        log.setCreatedAt(LocalDateTime.now());
        log.setLastUpdatedAt(LocalDateTime.now());
        notificationLogRepository.save(log);
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
    @Override
    public List<NotificationRequestLog> getNotificationLogs(String clientName){
        return notificationLogRepository.findByClientName(clientName);
    }

}
