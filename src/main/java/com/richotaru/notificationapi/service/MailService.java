package com.richotaru.notificationapi.service;

import org.apache.commons.mail.Email;

public interface MailService {

    void send(Email email);
}
