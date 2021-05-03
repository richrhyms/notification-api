package com.richotaru.notificationapi.controller;

import com.richotaru.notificationapi.IntegrationTest;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class NotificationControllerTest  extends IntegrationTest {

    @Test
    void sendEmailNotification() throws Exception {
//        mockMvc.perform(
//                post("/api/v1/notification/email"))
//                .andExpect(status().isOk())
//                .andExpect(result -> {
////                    assertEquals("Bolus", response.getLastName());
//                });
    }

    @Test
    void sendSmsNotification() {
    }
}
