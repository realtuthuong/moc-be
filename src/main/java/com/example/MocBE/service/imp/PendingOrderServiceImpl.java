package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.dto.request.StaffOrderRequest;
import com.example.MocBE.service.PendingOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PendingOrderServiceImpl implements PendingOrderService {

    private static final String PREFIX = "pendingOrder:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void savePending(String txnRef, CustomerOrderReservationRequest request) {
        String key = PREFIX + txnRef;
        redisTemplate.opsForValue().set(key, request, 30, TimeUnit.MINUTES);
    }

    @Override
    public CustomerOrderReservationRequest getPendingByTxnRef(String txnRef) {
        String key = PREFIX + txnRef;
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) return null;
        if (obj instanceof CustomerOrderReservationRequest) {
            return (CustomerOrderReservationRequest) obj;
        } else {
            // Đảm bảo luôn convertValue đúng kiểu
            return objectMapper.convertValue(obj, CustomerOrderReservationRequest.class);
        }
    }

    @Override
    public void deletePending(String txnRef) {
        String key = PREFIX + txnRef;
        redisTemplate.delete(key);
    }
}
