package com.springboot.jobportal.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendVerificationToken(String receiverEmail, String token) throws MessagingException;
}
