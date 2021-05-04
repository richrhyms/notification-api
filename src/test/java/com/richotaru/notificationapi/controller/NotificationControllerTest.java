package com.richotaru.notificationapi.controller;

import com.google.gson.Gson;
import com.richotaru.notificationapi.IntegrationTest;
import com.richotaru.notificationapi.domain.dto.EmailRequestDto;
import com.richotaru.notificationapi.domain.dto.SmsRequestDto;
import com.richotaru.notificationapi.entity.ClientAccount;
import com.richotaru.notificationapi.entity.SubscriptionPlan;
import com.richotaru.notificationapi.enumeration.GenericStatusConstant;
import com.richotaru.notificationapi.enums.PricingPlan;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class NotificationControllerTest  extends IntegrationTest {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    //Test to ensure a free account can not send beyond the sp
    @Test
    void sendEmailNotificationAsFreeAccount() throws Exception {


        EmailRequestDto dto = getEmailDto();
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
        SmsRequestDto dto = getSmsDto();
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

    //Test to ensure a BASIC account cannot send more than permitted limit
    @Test
    void sendEmailAndSmsNotificationAsBasicAccount() throws Exception {

        Long emailRequestLimitBasic= 1800L;
        Long smsRequestLimitBasic = 1200L;
        ClientAccount basicClientAccount = createBasicClientAccount(emailRequestLimitBasic,smsRequestLimitBasic );

        Long minuteEmailLimit = getMinuteLimit(emailRequestLimitBasic);

        EmailRequestDto dto = getEmailDto();
        logger.info(" Email minute Limit == > "+ minuteEmailLimit);
        for(int i=0; i< minuteEmailLimit; i++){
            //Expected Okay
            mockMvc.perform(
                    post("/api/v1/notification/email")
                            .content(new Gson().toJson(dto)).contentType(MediaType.APPLICATION_JSON)
                            .header("X-api-key",basicClientAccount.getApiKey()))
                    .andExpect(status().isOk());
        }
        //Expected Error
        mockMvc.perform(
                post("/api/v1/notification/email")
                        .content(new Gson().toJson(dto)).contentType(MediaType.APPLICATION_JSON)
                        .header("X-api-key",basicClientAccount.getApiKey()))
                .andExpect(status().isTooManyRequests());
    }

    private EmailRequestDto getEmailDto(){
        EmailRequestDto dto = new EmailRequestDto();
        dto.setSenderName("Test Sender");
        dto.setSenderEmail("test@email.com");
        dto.setSubject("test subject");
        dto.setMessage("Testing message");
        List<String> recipient = new ArrayList<>();
        recipient.add("me@irembo.com");
        dto.setRecipientEmails(recipient);
        return dto;
    }
    private SmsRequestDto getSmsDto(){
        SmsRequestDto dto = new SmsRequestDto();
        dto.setMessage("Test Message");
        List<String> recipient = new ArrayList<>();
        recipient.add("07032804231");
        dto.setRecipientPhoneNumbers(recipient);
        return dto;
    }
    private ClientAccount createBasicClientAccount(Long emailRequestLimitBasic, Long smsRequestLimitBasic){
        // CREATING BASIC CLIENT
       return clientAccountRepository.findByDisplayNameAndStatus("TEST_BASIC_CLIENT", GenericStatusConstant.ACTIVE)
                .orElseGet(() -> {
                    SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByName(PricingPlan.BASIC.name() + "TEST")
                            .orElseGet(
                                    ()->{
                                        SubscriptionPlan plan =  new SubscriptionPlan();

                                        plan.setName(PricingPlan.BASIC.name());
                                        plan.setDescription(PricingPlan.BASIC.name() + " Service");
                                        plan.setMaxMonthlyEmailLimit(emailRequestLimitBasic);
                                        plan.setMaxMonthlySmsLimit(smsRequestLimitBasic);
                                        plan.setStatus(GenericStatusConstant.ACTIVE);
                                        plan.setDateDeactivated(LocalDateTime.now());
                                        plan.setCreatedAt(LocalDateTime.now());
                                        plan.setLastUpdatedAt(LocalDateTime.now());
                                        appRepository.persist(plan);
                                        return plan;
                                    });

                    ClientAccount clientSystem = new ClientAccount();
                    clientSystem.setDisplayName("TEST_BASIC_CLIENT");
                    clientSystem.setEmail("richotaru@gmail.com");
                    clientSystem.setPhoneNumber("+2347032804231");
                    clientSystem.setStatus(GenericStatusConstant.ACTIVE);
                    clientSystem.setCreatedAt(LocalDateTime.now());
                    clientSystem.setLastUpdatedAt(LocalDateTime.now());
                    clientSystem.setSubscriptionPlan(subscriptionPlan);

                    // GENERATING FAKE API-KEY
                    clientSystem.setApiKey("test-key-2a9a428a");
                    appRepository.persist(clientSystem);
                    return clientSystem;
                });
    }

    private Long getMinuteLimit(Long monthLyLimit){
        long l2 = Math.floorDiv(monthLyLimit, 30);
        long l1 = Math.floorDiv(l2, 24);
        Long l =  Math.floorDiv(l1, 60);
        return l > 2 ? l : 2;
    }
}
