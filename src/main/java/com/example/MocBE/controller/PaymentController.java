package com.example.MocBE.controller;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;

    // Khách hàng đặt bàn online
    @PostMapping("/customerOrder")
    String CustomerPayment(HttpServletRequest req, @RequestBody CustomerOrderReservationRequest orderRequest)  throws UnsupportedEncodingException {
        paymentService.validateOrderReservation(orderRequest);
        BigDecimal totalPayment =  paymentService.calculateTotal(orderRequest);
        return paymentService.payment(req, totalPayment, orderRequest);
    }

    @GetMapping("/info")
    public ResponseEntity<String> transaction(HttpServletRequest request) {
        return paymentService.processVNPayCallback(request);
    }

}

