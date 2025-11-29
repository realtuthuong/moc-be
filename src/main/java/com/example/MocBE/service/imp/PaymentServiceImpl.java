package com.example.MocBE.service.imp;

import com.example.MocBE.config.VNPayConfig;
import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.dto.request.StaffOrderRequest;
import com.example.MocBE.model.MenuItem;
import com.example.MocBE.repository.*;
import com.example.MocBE.service.EmailService;
import com.example.MocBE.service.OrderService;
import com.example.MocBE.service.PaymentService;
import com.example.MocBE.service.PendingOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final MenuItemRepository menuItemRepository;
    private final PendingOrderService pendingOrderService;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final LocationRepository locationRepository;
    private final OrderService orderService;
    private final EmailService emailService;

    @Override
    public String payment(HttpServletRequest req, BigDecimal totalVNPay, CustomerOrderReservationRequest orderRequest)  throws UnsupportedEncodingException {
        String orderType = "Thanh toán VNPay";
        long amount = totalVNPay.multiply(BigDecimal.valueOf(100)).longValue();
        String bankCode = req.getParameter("bankCode");

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.getVnpTmnCode();

        // Lưu đơn tạm vào Redis
        pendingOrderService.savePending(vnp_TxnRef, orderRequest);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.getVnpTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.getVnpReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_OrderType", orderType);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.getSecretKey(), hashData.toString());
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;

    }

    @Override
    public BigDecimal calculateTotal(CustomerOrderReservationRequest orderRequest) {
        if (orderRequest.getListItem() == null || orderRequest.getListItem().isEmpty()) {
            throw new RuntimeException("Danh sách món ăn không được để trống.");
        }

        BigDecimal totalVNPay = BigDecimal.ZERO;
        for (CustomerOrderReservationRequest.ItemRequest item : orderRequest.getListItem()) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn."));

            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalVNPay = totalVNPay.add(itemTotal);
        }
        return totalVNPay;
    }



    @Override
    public void validateOrderReservation(CustomerOrderReservationRequest orderRequest) {
        if (orderRequest == null) {
            throw new RuntimeException("Dữ liệu đặt bàn không hợp lệ.");
        }

        LocalDate reservationDateOnly = orderRequest.getReservationDate().toLocalDate();
        boolean isReserved = orderRepository.existsByTableIdAndReservationDate(
                orderRequest.getTableId(),
                reservationDateOnly
        );
        if (isReserved) {
            throw new RuntimeException("Bàn này đã được đặt trong ngày " + reservationDateOnly + ". Vui lòng chọn bàn hoặc ngày khác.");
        }
        locationRepository.findById(orderRequest.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực."));
        tableRepository.findById(orderRequest.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn."));
    }

    @Override
    public ResponseEntity<String> processVNPayCallback(HttpServletRequest request) {
        String rawQuery = request.getQueryString();

        if (rawQuery == null || rawQuery.isEmpty()) {
            return ResponseEntity.badRequest().body("Thiếu dữ liệu callback từ VNPay!");
        }
        Map<String, String> allParams = new LinkedHashMap<>();
        for (String pair : rawQuery.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                allParams.put(key, value);
            }
        }

        String vnp_SecureHash = allParams.get("vnp_SecureHash");
        allParams.remove("vnp_SecureHash");
        allParams.remove("vnp_SecureHashType");

        // Tạo chuỗi hash (sử dụng value đã encode)
        String hashData = VNPayConfig.hashAllFields(allParams);
        String signValue = VNPayConfig.hmacSHA512(VNPayConfig.getSecretKey().trim(), hashData);

        System.out.println("HashData: " + hashData);
        System.out.println("Sign: " + signValue);
        System.out.println("vnp_SecureHash: " + vnp_SecureHash);

        if (!signValue.equalsIgnoreCase(vnp_SecureHash)) {
            return ResponseEntity.badRequest().body("Chữ ký không hợp lệ!");
        }

        String txnRef = allParams.get("vnp_TxnRef");
        CustomerOrderReservationRequest orderRequest = pendingOrderService.getPendingByTxnRef(txnRef);

        if (orderRequest == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy đơn hàng khớp mã: " + txnRef);
        }

        String responseCode = allParams.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            orderService.createCustomerOrder(orderRequest);

            emailService.sendOrderConfirmationEmail(
                    txnRef,
                    orderRequest,
                    calculateTotal(orderRequest)
            );

            pendingOrderService.deletePending(txnRef);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:3000/public/payment-success?orderId=" + txnRef))
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:3000/public/payment-failed?orderId=" + txnRef
                            + "&errorCode=" + responseCode))
                    .build();
        }

    }



}
