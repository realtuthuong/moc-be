package com.example.MocBE.controller;

import com.example.MocBE.dto.request.OrderFilterRequest;
import com.example.MocBE.dto.request.StaffOrderRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public WrapResponse<PageResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int size,
            OrderFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderService.getAllOrders(filterRequest, pageable);
    }

    // nhân viên gọi món tại quán
    @PostMapping("/staffOrder")
    ResponseEntity<?> StaffPayment(@RequestBody StaffOrderRequest staffOrderRequest) {
        orderService.createOrderStaff(staffOrderRequest);
        return ResponseEntity.ok("Order thành công!");
    }

    @GetMapping("/today")
    public WrapResponse<List<OrderResponse>> getOrdersToday() {
        return orderService.getOrdersToday();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable UUID id) {
        orderService.completeOrder(id);
        return ResponseEntity.ok("Cập nhật trạng thái thành công!");
    }

    @GetMapping("/revenue/current")
    public WrapResponse<RevenueResponse> getCurrentRevenue() {
        return orderService.getCurrentRevenue();
    }

    @GetMapping("/total/current")
    public WrapResponse<TotalOrderResponse> getCurrentOrdersCount() {
        return orderService.getCurrentOrdersCount();
    }

    @GetMapping("/status/today")
    public WrapResponse<TotalOrderStatusTodayResponse> getTotalOrderStatusToday() {
        return orderService.getTotalOrderStatusToday();
    }

    @GetMapping("/revenue/hour/all")
    public WrapResponse<List<RevenueByHourAllTimeResponse>> getRevenueByHourAllTime() {
        return orderService.getRevenueByHourAllTime();
    }

    // get order hôm nay by locationId
    @GetMapping("/today/{locationId}")
    public WrapResponse<List<OrderResponse>> getOrdersTodayByLocation(@PathVariable UUID locationId) {
        return orderService.getOrdersTodayByLocation(locationId);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable UUID customerId) {
        List<OrderResponse> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }
}