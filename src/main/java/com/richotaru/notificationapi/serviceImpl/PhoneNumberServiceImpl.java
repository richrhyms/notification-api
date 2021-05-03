package com.richotaru.notificationapi.serviceImpl;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.richotaru.notificationapi.service.PhoneNumberService;


public class PhoneNumberServiceImpl implements PhoneNumberService {

    PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            return null;
        }
        if(!phoneNumber.startsWith("+")){
            phoneNumber = "+"+phoneNumber.trim();
        }
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber.replaceAll(" +", ""), "NG");
            return phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isValid(String value) {

        if (value == null) {
            return true;
        }

        Phonenumber.PhoneNumber swissNumberProto;
        try {
            swissNumberProto = phoneNumberUtil.parse(value.trim(), "US");
        } catch (NumberParseException e) {
            return false;
        }

        return phoneNumberUtil.isValidNumber(swissNumberProto);
    }
}
