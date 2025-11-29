package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;
import com.example.MocBE.dto.response.NotificationResponse;
import com.example.MocBE.model.Customer;
import com.example.MocBE.model.Location;
import com.example.MocBE.model.Table;
import com.example.MocBE.repository.CustomerRepository;
import com.example.MocBE.repository.LocationRepository;
import com.example.MocBE.repository.OrderRepository;
import com.example.MocBE.repository.TableRepository;
import com.example.MocBE.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;
    private final LocationRepository locationRepository;
    private final OrderRepository orderRepository;

    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public NotificationResponse sendEmailToAllCustomers(String subject, String content) {
        List<Customer> customers = customerRepository.findAll();
        if (customers == null || customers.isEmpty()) {
            throw new RuntimeException("Danh sách khách hàng trống hoặc không tìm thấy!");
        }
        int successCount = 0;
        int failCount = 0;
        for (Customer customer : customers) {
            try {
                sendEmail(customer.getEmail(), subject, content);
                successCount++;
            } catch (Exception e) {
                logger.error("Gửi email đến khách hàng {} thất bại: {}", customer.getEmail(), e.getMessage());
                failCount++;
            }
        }

        return new NotificationResponse(
                failCount == 0 ? "success" : "partial_success",
                subject,
                content,
                String.format("Gửi email thành công đến %d khách hàng, thất bại %d khách hàng.", successCount, failCount)
        );
    }

    public void sendVerificationEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Xác nhận email của bạn");
        message.setText("Click vào đường link để xác nhận email của bạn: " + link);
        mailSender.send(message);
    }


    public void sendOrderConfirmationEmail(String txnRef, CustomerOrderReservationRequest orderRequest, BigDecimal totalAmount) {
        StringBuilder content = new StringBuilder();
        Table table = tableRepository.getById(orderRequest.getTableId());
        Location location = locationRepository.getById(orderRequest.getLocationId());
        if(table == null & location == null){
            throw new RuntimeException("Bàn không tìm thấy!");
        }
        content
                .append("Cảm ơn bạn đã thanh toán thành công đơn hàng tại nhà hàng của chúng tôi!\n\n")
                .append("**Thông tin đơn hàng**\n")
                .append("• Mã giao dịch: ").append(txnRef).append("\n")

                .append("• Ngày đặt: ").append(orderRequest.getReservationDate()).append("\n")
                .append("• Bàn: ").append(table.getName()).append("\n")
                .append("• Khu vực: ").append(location.getName()).append("\n\n");

        content.append("\n**Tổng tiền đã thanh toán:** ")
                .append(totalAmount.toPlainString())
                .append(" VND\n\n");

        content.append("Chúc bạn có bữa ăn ngon miệng!\nNhà hàng Moc Restaurant.");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(orderRequest.getEmail());
        message.setSubject("Xác nhận thanh toán thành công - Đơn hàng #" + txnRef);
        message.setText(content.toString());

        mailSender.send(message);
    }

}

