package com.richotaru.notificationapi.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SmsRequestDto {
    @NotBlank(message = "message cannot be blank")
    private String message;
    @Size(min = 1, message = "You must provide at least one recipient Phone Number")
    private List<String> recipientPhoneNumbers;
}
