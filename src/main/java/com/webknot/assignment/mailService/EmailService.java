package com.webknot.assignment.mailService;

import com.webknot.assignment.model.email.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
}