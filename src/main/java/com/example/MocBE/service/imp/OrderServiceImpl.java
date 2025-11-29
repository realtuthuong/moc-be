package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.dto.request.OrderFilterRequest;
import com.example.MocBE.dto.request.StaffOrderRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.enums.InvoiceStatus;
import com.example.MocBE.enums.OrderStatus;
import com.example.MocBE.enums.OrderType;
import com.example.MocBE.enums.TableStatus;
import com.example.MocBE.mapper.OrderMapper;
import com.example.MocBE.model.*;
import com.example.MocBE.repository.*;
//import com.example.MocBE.service.KafkaProducerService;
import com.example.MocBE.repository.spec.OrderSpecification;
import com.example.MocBE.service.OrderService;
import com.example.MocBE.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final LocationRepository locationRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AccountRepository accountRepository;
    private final OrderMapper orderMapper;
    private final SimpMessagingTemplate messagingTemplate;

    //    private final KafkaProducerService kafkaProducerService;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void sendKafka(UUID orderId) {
//        kafkaProducerService.sendOrderCreatedEvent(orderId);
//    }

    // khách hàng order and food bàn trước
    @Transactional
    @Override
    public void createCustomerOrder(CustomerOrderReservationRequest orderRequest)  {

        // Lấy thông tin bàn, khu vực
        Location location = locationRepository.findById(orderRequest.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khu vực."));
        Table table = tableRepository.findById(orderRequest.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn."));

        // save customer
        Customer customer = Customer.builder()
                .fullName(orderRequest.getFullName())
                .email(orderRequest.getEmail())
                .phone(orderRequest.getPhone())
                .build();
        customerRepository.save(customer);

        // Tạo invoice rỗng ban đầu
        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.DA_THANH_TOAN);
        invoice.setTotalAmount(BigDecimal.ZERO);  // cập nhật sau
        invoiceRepository.save(invoice);

        // Tạo Order
        Order order = new Order();
        order.setCustomer(customer);
        order.setTable(table);
        order.setLocation(location);
        order.setInvoice(invoice);
        order.setOrderType(OrderType.đặt_bàn_trước);
        order.setStatusOrder(OrderStatus.DA_HOAN_THANH);
        order.setReservationDate(orderRequest.getReservationDate());
        order.setGuestCount(orderRequest.getGuestCount());
        order.setNote(orderRequest.getNote());
        order.setTotalPrice(BigDecimal.ZERO);
        orderRepository.save(order);

        // Lưu danh sách món và tính tổng
        BigDecimal total = BigDecimal.ZERO;
        for (CustomerOrderReservationRequest.ItemRequest item : orderRequest.getListItem()) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn."));

            // giá * số lượng
            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setMenuItem(menuItem);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setNote(item.getNote());
            orderDetail.setPriceAtOrder(menuItem.getPrice());
            orderDetailRepository.save(orderDetail);
        }

        // Cập nhật tổng tiền vào Order và Invoice
        order.setTotalPrice(total);
        invoice.setTotalAmount(total);
        table.setStatus(TableStatus.CO_KHACH);
        orderRepository.save(order);
        invoiceRepository.save(invoice);
    }

    // nhân viên order tại chỗ
    @Transactional
    @Override
    public void createOrderStaff(StaffOrderRequest staffOrderRequest) {
        if (staffOrderRequest == null) {
            throw new RuntimeException("Dữ liệu đặt bàn không hợp lệ.");
        }

        Account account = accountRepository.findById(staffOrderRequest.getAccountId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account."));
        Table table = tableRepository.findById(staffOrderRequest.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn."));

        // Tạo invoice rỗng ban đầu
        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.DA_THANH_TOAN);
        invoice.setTotalAmount(BigDecimal.ZERO);
        invoiceRepository.save(invoice);

        Location location = account.getLocation();
        Order order = new Order();
        order.setCustomer(null);
        order.setTable(table);
        order.setLocation(location);
        order.setInvoice(invoice);
        order.setAccount(account);
        order.setReservationDate(
                staffOrderRequest.getReservationDate() != null
                        ? staffOrderRequest.getReservationDate()
                        : LocalDateTime.now()
        );
        order.setOrderType(OrderType.tại_quán);
        order.setStatusOrder(OrderStatus.DANG_CHUAN_BI);
        order.setGuestCount(staffOrderRequest.getGuestCount());
        order.setNote(staffOrderRequest.getNote());
        order.setTotalPrice(BigDecimal.ZERO);   // cập nhật sau
        orderRepository.save(order);

        // Lưu danh sách món và tính tổng
        BigDecimal total = BigDecimal.ZERO;
        for (CustomerOrderReservationRequest.ItemRequest item : staffOrderRequest.getListItem()) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn."));

            // giá * số lượng
            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setMenuItem(menuItem);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setNote(item.getNote());
            orderDetail.setPriceAtOrder(menuItem.getPrice());
            orderDetailRepository.save(orderDetail);
        }

        // Cập nhật tổng tiền vào Order và Invoice
        order.setTotalPrice(total);
        invoice.setTotalAmount(total);
        // sendKafka(order.getId());
        orderRepository.save(order);
        invoiceRepository.save(invoice);
        table.setStatus(TableStatus.CO_KHACH);
        tableRepository.save(table);
    }

    @Override
    public WrapResponse<PageResponse<OrderResponse>> getAllOrders(OrderFilterRequest filterRequest, Pageable pageable) {
        logger.info("Bắt đầu lấy danh sách order có filter và phân trang");
        try {
            Specification<Order> spec = Specification
                    .where(OrderSpecification.hasStatus(filterRequest.getStatusOrder()))
                    .and(OrderSpecification.hasType(filterRequest.getOrderType()))
                    .and(OrderSpecification.customerNameContains(filterRequest.getCustomerName()))
                    .and(OrderSpecification.createdAfter(filterRequest.getStartDate()))
                    .and(OrderSpecification.createdBefore(filterRequest.getEndDate()));

            Page<OrderResponse> orderPage = orderRepository.findAll(spec, pageable)
                    .map(orderMapper::todo);

            PageResponse<OrderResponse> pageResponse = PageResponse.<OrderResponse>builder()
                    .totalItems(orderPage.getTotalElements())
                    .totalPages(orderPage.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1) // chú ý +1 để trả về page thật
                    .objectJson(orderPage.getContent())
                    .build();

            return WrapResponse.<PageResponse<OrderResponse>>builder()
                    .requestId(UUID.randomUUID().toString())
                    .requestAction("GET_ALL_ORDERS")
                    .error(false)
                    .objectJson(pageResponse)
                    .build();

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách order có filter và phân trang: ", ex);

            return WrapResponse.<PageResponse<OrderResponse>>builder()
                    .requestId(UUID.randomUUID().toString())
                    .requestAction("GET_ALL_ORDERS")
                    .error(true)
                    .errorType("EXCEPTION")
                    .errorReason(ex.getMessage())
                    .toastMessage("Không thể lấy danh sách order!")
                    .build();
        }
    }


    @Override
    public WrapResponse<List<OrderResponse>> getOrdersToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<Order> orders = orderRepository.findAllWithDetailsByReservationDateBetween(startOfDay, endOfDay);

        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::todo) // OrderMapper phải map cả listOrderDetail
                .toList();

        return WrapResponse.<List<OrderResponse>>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_ORDERS_TODAY")
                .error(false)
                .objectJson(responses)
                .build();
    }

    @Transactional
    @Override
    public void completeOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy order."));

        order.setStatusOrder(OrderStatus.DA_HOAN_THANH);
        orderRepository.save(order);
    }

    @Override
    public WrapResponse<RevenueResponse> getCurrentRevenue() {
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        BigDecimal totalRevenueMonth = orderRepository.getTotalRevenueByMonth(currentMonth, currentYear);
        BigDecimal totalRevenueYear = orderRepository.getTotalRevenueByYear(currentYear);

        RevenueResponse revenueResponse = RevenueResponse.builder()
                .totalRevenueMonth(totalRevenueMonth)
                .totalRevenueYear(totalRevenueYear)
                .build();

        return WrapResponse.<RevenueResponse>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_CURRENT_REVENUE")
                .error(false)
                .objectJson(revenueResponse)
                .build();
    }

    @Override
    public WrapResponse<TotalOrderResponse> getCurrentOrdersCount() {
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        Long totalOrdersMonth = orderRepository.getTotalOrdersByMonth(currentMonth, currentYear);
        Long totalOrdersYear = orderRepository.getTotalOrdersByYear(currentYear);

        TotalOrderResponse response = TotalOrderResponse.builder()
                .totalOrdersMonth(totalOrdersMonth)
                .totalOrdersYear(totalOrdersYear)
                .build();

        return WrapResponse.<TotalOrderResponse>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_CURRENT_ORDER_STATS")
                .error(false)
                .objectJson(response)
                .build();
    }

    @Override
    public WrapResponse<TotalOrderStatusTodayResponse> getTotalOrderStatusToday() {
        Long totalDangChuanBi = orderRepository.countDangChuanBiToday();
        Long totalDaHoanThanh = orderRepository.countDaHoanThanhToday();

        TotalOrderStatusTodayResponse response = TotalOrderStatusTodayResponse.builder()
                .totalDangChuanBiToday(totalDangChuanBi)
                .totalDaHoanThanhToday(totalDaHoanThanh)
                .build();

        return WrapResponse.<TotalOrderStatusTodayResponse>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_TOTAL_ORDER_STATUS_TODAY")
                .error(false)
                .objectJson(response)
                .build();
    }

    @Override
    public WrapResponse<List<RevenueByHourAllTimeResponse>> getRevenueByHourAllTime() {
        List<Object[]> results = orderRepository.getRevenueByHourAllTime();

        List<RevenueByHourAllTimeResponse> response = results.stream()
                .map(r -> new RevenueByHourAllTimeResponse(
                        ((Integer) r[0]),
                        (BigDecimal) r[1]
                ))
                .toList();

        return WrapResponse.<List<RevenueByHourAllTimeResponse>>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_REVENUE_BY_HOUR_ALL_TIME")
                .error(false)
                .objectJson(response)
                .build();
    }

    @Override
    public WrapResponse<List<OrderResponse>> getOrdersTodayByLocation(UUID locationId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<Order> orders = orderRepository
                .findAllByReservationDateBetweenAndLocation_Id(startOfDay, endOfDay, locationId);

        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::todo) // nhớ mapper phải map cả listOrderDetail
                .toList();

        return WrapResponse.<List<OrderResponse>>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_ORDERS_TODAY_BY_LOCATION")
                .error(false)
                .objectJson(responses)
                .build();
    }

    public List<OrderResponse> getOrdersByCustomerId(UUID customerId) {
        List<Order> orders = orderRepository.findByCustomer_Id(customerId);
        return orders.stream()
                .map(orderMapper::todo)
                .toList();
    }
}
