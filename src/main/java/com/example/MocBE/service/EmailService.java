package com.example.MocBE.service;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.dto.response.NotificationResponse;
import com.example.MocBE.model.Customer;

import java.math.BigDecimal;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
    NotificationResponse sendEmailToAllCustomers(String subject, String content);
    void sendVerificationEmail(String to, String link);
    void sendOrderConfirmationEmail(String txnRef, CustomerOrderReservationRequest orderRequest, BigDecimal totalAmount);
}
