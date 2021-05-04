package com.richotaru.notificationapi.controller;

import com.google.gson.Gson;
import com.richotaru.notificationapi.IntegrationTest;
import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class NotificationControllerTest  extends IntegrationTest {
    @Value("${EMAIL_LIMIT_FREE:2}")
    private Long emailRequestLimitFree;
    @Value("${SMS_LIMIT_FREE:2}")
    private Long smsRequestLimitFree;

    //Test to ensure a free account can not send beyond the sp
    @Test
    void sendEmailNotificationAsFreeAccount() throws Exception {


        EmailRequestDto dto = new EmailRequestDto();
        dto.setSenderName("Test Sender");
        dto.setSenderEmail("test@email.com");
        dto.setSubject("test subject");
        dto.setMessage("Testing message");
        List<String> recipient = new ArrayList<>();
        recipient.add("me@irembo.com");
        dto.setRecipientEmails(recipient);
        for(int i=0; i< emailRequestLimitFree; i++){
            //Expected Okay
            mockMvc.perform(
                    post("/api/v1/notification/email")
                            .content(new Gson().toJson(dto)).contentType(MediaType.APPLICATION_JSON)
                            .header("X-api-key","no-key"))
                    .andExpect(status().isOk());
        }

        //Expected Error
        mockMvc.perform(
                post("/api/v1/notification/email")
                        .content(new Gson().toJson(dto)).contentType(MediaType.APPLICATION_JSON)
                        .header("X-api-key","no-key"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void sendSmsNotificationAsFreeAccount() throws Exception {
        SmsRequestDto dto = new SmsRequestDto();
        dto.setMessage("Test Message");
        List<String> recipient = new ArrayList<>();
        recipient.add("07032804231");
        dto.setRecipientPhoneNumbers(recipient);

        for(int i=0; i< smsRequestLimitFree; i++){
            //Expected Okay
            mockMvc.perform(
                    post("/api/v1/notification/sms")
                            .content(new Gson().toJson(dto)).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
        //Expected Error
        mockMvc.perform(
                post("/api/v1/notification/sms")
                        .content(new Gson().toJson(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isTooManyRequests());
    }
}
