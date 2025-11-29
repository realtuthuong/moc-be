package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.CustomerFilterRequest;
import com.example.MocBE.dto.request.CustomerRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.exception.RegistrationException;
import com.example.MocBE.mapper.CustomerMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.Customer;
import com.example.MocBE.repository.CustomerRepository;
import com.example.MocBE.repository.spec.CustomerSpecification;
import com.example.MocBE.service.CustomerService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final WrapResponseFactory wrapResponseFactory;

    @Override
    public WrapResponse<PageResponse<CustomerResponse>> getAllCustomers(CustomerFilterRequest filterRequest, Pageable pageable) {
        try {
            logger.info("Lấy danh sách customer phân trang với filter");

            var spec = CustomerSpecification.filter(filterRequest);

            Page<CustomerResponse> customerPage = customerRepository.findAll(spec, pageable)
                    .map(customerMapper::toDto);

            PageResponse<CustomerResponse> pageResponse = PageResponse.<CustomerResponse>builder()
                    .totalItems(customerPage.getTotalElements())
                    .totalPages(customerPage.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(customerPage.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_CUSTOMERS");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách customer phân trang: ", ex);
            return wrapResponseFactory.error(
                    "GET_CUSTOMERS",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy khách hàng có ID: {}", id);
                    return new RuntimeException("Không tìm thấy khách hàng có ID: " + id);
                });
        customer.setIsDeleted(true);
        customerRepository.save(customer);
        SuccessResponse response = ResponseUtil.success(
                "Xóa khách hàng thành công",
                "/api/customer/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> updateCustomer(CustomerRequest request)  {
        UUID id = request.getId();
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy khách hàng để cập nhật: {}", id);
                    return new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
                });

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setDateOfBirth(request.getDateOfBirth());

        customerRepository.save(customer);

        return ResponseEntity
                .ok(ResponseUtil.success("Cập nhật thành công", "/api/customer/" + id));
    }


    @Override
    public ResponseEntity<SuccessResponse> createCustomer(CustomerRequest request)  {
        if (customerRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RegistrationException("Số điện thoai đã tồn tại!");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email đã được sử dụng!");
        }
        Customer customer = Customer.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .isDeleted(false)
                .build();


        customerRepository.save(customer);

        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/customer");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public WrapResponse<TotalCustomerResponse> getTotalCustomers() {
        Long total = customerRepository.countAllActiveCustomers();

        TotalCustomerResponse response = TotalCustomerResponse.builder()
                .totalCustomers(total)
                .build();

        return WrapResponse.<TotalCustomerResponse>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_TOTAL_CUSTOMERS")
                .error(false)
                .objectJson(response)
                .build();
    }

}
