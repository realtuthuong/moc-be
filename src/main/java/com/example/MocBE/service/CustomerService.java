package com.example.MocBE.service;

import com.example.MocBE.dto.request.CreateAccountRequest;
import com.example.MocBE.dto.request.CustomerFilterRequest;
import com.example.MocBE.dto.request.CustomerRequest;
import com.example.MocBE.dto.request.UpdateAccountRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;

public interface CustomerService {
    WrapResponse<PageResponse<CustomerResponse>> getAllCustomers(CustomerFilterRequest filterRequest, Pageable pageable);
    ResponseEntity<SuccessResponse> deleteCustomer(UUID id);
    ResponseEntity<SuccessResponse> updateCustomer(CustomerRequest request) throws IOException;
    ResponseEntity<SuccessResponse> createCustomer(CustomerRequest request) throws IOException;
    WrapResponse<TotalCustomerResponse> getTotalCustomers();
}
