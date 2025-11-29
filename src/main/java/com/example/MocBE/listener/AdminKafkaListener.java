//package com.example.MocBE.listener;
//
//import com.example.MocBE.model.Order;
//import com.example.MocBE.repository.OrderRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class AdminKafkaListener {
//
//    private final SimpMessagingTemplate messagingTemplate;
//    private final OrderRepository orderRepository;
//
//    private static final Logger logger = LoggerFactory.getLogger(AdminKafkaListener.class);
//
//    @KafkaListener(topics = "new-orders", groupId = "admin-group")
//    public void handleNewOrder(String message) {
//        System.out.println("Kafka nhận message: " + message);
//
//        try {
//            UUID orderId = UUID.fromString(message.replace("NEW_ORDER_ID_", ""));
//            Optional<Order> optionalOrder = orderRepository.findById(orderId);
//
//            if (optionalOrder.isPresent()) {
//                Order order = optionalOrder.get();
//
//                Map<String, Object> orderData = new HashMap<>();
//                orderData.put("id", order.getId());
//                orderData.put("note", order.getNote());
//                orderData.put("totalPrice", order.getTotalPrice());
//                orderData.put("status", order.getStatusOrder().toString());
//
//                messagingTemplate.convertAndSend("/topic/new-orders", orderData);
//                System.out.println("Đã gửi WebSocket đơn hàng: " + orderData);
//            } else {
//                System.out.println("Không tìm thấy đơn hàng với ID: " + orderId);
//            }
//        } catch (Exception e) {
//            System.out.println("Lỗi Kafka message: " + e.getMessage());
//        }
//    }
//
//}
