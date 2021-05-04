package com.richotaru.notificationapi;


import com.richotaru.notificationapi.dao.AppRepository;
import com.richotaru.notificationapi.dao.ClientAccountRepository;
import com.richotaru.notificationapi.dao.SubscriptionPlanRepository;
import com.richotaru.notificationapi.service.NotificationService;
import com.richotaru.notificationapi.service.SettingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;

@AutoConfigureMockMvc
@SpringBootTest
public abstract class IntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected SettingService settingService;
    @Autowired
    protected AppRepository appRepository;
    @Autowired
    protected SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    protected ClientAccountRepository clientAccountRepository;
    @MockBean
    protected NotificationService notificationService;
    static LocalDateTime startTime;
    static LocalDateTime endTime;

    protected Long emailRequestLimitFree;
    protected Long smsRequestLimitFree;
    @BeforeEach
    public void before() {
        startTime = LocalDateTime.now();
        Mockito.doNothing().when(notificationService).sendEmail(Mockito.any());
        Mockito.doNothing().when(notificationService).sendSms(Mockito.any());
    }

    @AfterEach
    public void after() {
        endTime = LocalDateTime.now();

        System.out.println("----------------- STATS -----------------");
        System.out.println(String.format("Test start time : %s", startTime));
        System.out.println(String.format("Test end time   : %s", endTime));
        System.out.println(String.format("Execution time  : %sms", Duration.between(startTime,endTime).toMillis()));
        System.out.println(String.format("Execution time in milliseconds : %sms", Duration.between(startTime,endTime).toMillis()));
        System.out.println("------------------------------------------");
    }
    @PostConstruct
    private void init() {
        emailRequestLimitFree = settingService.getLong("EMAIL_LIMIT_FREE", 2);
        smsRequestLimitFree = settingService.getLong("SMS_LIMIT_FREE", 2);

    }
}
