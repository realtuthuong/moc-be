package com.example.MocBE.controller;

import com.example.MocBE.dto.request.*;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public  WrapResponse<PageResponse<CustomerResponse>> getAllCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int size,
            CustomerFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return customerService.getAllCustomers(filterRequest, pageable);
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> createCustomer(@Valid @RequestBody CustomerRequest request) throws IOException {
        return customerService.createCustomer(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteCustomer(@PathVariable UUID id) {
        return customerService.deleteCustomer(id);
    }

    @PutMapping()
    public ResponseEntity<SuccessResponse> updateCustomer(@Valid @RequestBody CustomerRequest request) throws IOException {
        return customerService.updateCustomer(request);
    }

    @GetMapping("/total")
    public WrapResponse<TotalCustomerResponse> getTotalCustomers() {
        return customerService.getTotalCustomers();
    }
}
