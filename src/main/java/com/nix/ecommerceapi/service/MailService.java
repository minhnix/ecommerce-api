package com.nix.ecommerceapi.service;

public interface MailService {
    void sendMail(String to, String subject, String text);
}
