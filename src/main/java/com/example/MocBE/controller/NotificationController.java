package com.example.MocBE.controller;

import com.example.MocBE.dto.request.NotificationRequest;
import com.example.MocBE.dto.response.NotificationResponse;
import com.example.MocBE.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send-all")
    public NotificationResponse sendToAllCustomers(@RequestBody NotificationRequest request) {
        return emailService.sendEmailToAllCustomers(request.getSubject(), request.getContent());
    }
}
