package com.richotaru.notificationapi.domain.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class EmailRequestDto {
    @NotBlank(message = "Sender Name cannot be blank")
    private String senderName;
    @NotBlank(message = "Sender Email cannot be blank")
    private String senderEmail;
    @NotBlank(message = "Subject cannot be blank")
    private String subject;
    @NotBlank(message = "message cannot be blank")
    private String message;
    @Min(value = 1, message = "You must provide at least one recipient email")
    private List<@Email String> recipientEmails;
}
