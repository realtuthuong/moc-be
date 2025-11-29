package com.example.MocBE.service;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.dto.request.StaffOrderRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public interface PaymentService {

    // customer payment
    String payment(HttpServletRequest req, BigDecimal totalVNPay, CustomerOrderReservationRequest orderRequest)  throws UnsupportedEncodingException;
    BigDecimal calculateTotal(@RequestBody CustomerOrderReservationRequest orderRequest);
    void validateOrderReservation(CustomerOrderReservationRequest orderRequest);
    public ResponseEntity<String> processVNPayCallback(HttpServletRequest request);

}
