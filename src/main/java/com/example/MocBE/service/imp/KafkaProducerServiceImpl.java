//package com.example.MocBE.service.imp;
//
//import com.example.MocBE.service.KafkaProducerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaProducerServiceImpl implements KafkaProducerService {
//
//    private static final String TOPIC = "new-orders";
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    @Override
//    public void sendOrderCreatedEvent(UUID orderId) {
//        kafkaTemplate.send(TOPIC, "NEW_ORDER_ID_" + orderId);
//    }
//}