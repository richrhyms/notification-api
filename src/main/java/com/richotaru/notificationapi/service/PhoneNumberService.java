package com.richotaru.notificationapi.service;

public interface PhoneNumberService {

    String formatPhoneNumber(String phoneNumber);

    boolean isValid(String value);
}
