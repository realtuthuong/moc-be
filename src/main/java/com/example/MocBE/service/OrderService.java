package com.example.MocBE.service;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.dto.request.OrderFilterRequest;
import com.example.MocBE.dto.request.StaffOrderRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    WrapResponse<PageResponse<OrderResponse>> getAllOrders(OrderFilterRequest filterRequest, Pageable pageable);
    // dặt bàn trước
    void createCustomerOrder(CustomerOrderReservationRequest orderRequest) ;

    // staffs
    public void createOrderStaff(StaffOrderRequest staffOrderRequest);
    WrapResponse<List<OrderResponse>> getOrdersToday();

    void completeOrder(UUID orderId);
    WrapResponse<RevenueResponse> getCurrentRevenue();
    WrapResponse<TotalOrderResponse> getCurrentOrdersCount();
    WrapResponse<TotalOrderStatusTodayResponse> getTotalOrderStatusToday();
    WrapResponse<List<RevenueByHourAllTimeResponse>> getRevenueByHourAllTime();
    WrapResponse<List<OrderResponse>> getOrdersTodayByLocation(UUID locationId);
    List<OrderResponse> getOrdersByCustomerId(UUID customerId);
}
