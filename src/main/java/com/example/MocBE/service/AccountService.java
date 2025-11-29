package com.example.MocBE.service;

import com.example.MocBE.dto.request.AccountFilterRequest;
import com.example.MocBE.dto.request.CreateAccountRequest;
import com.example.MocBE.dto.request.UpdateAccountRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    WrapResponse<PageResponse<AccountResponse>> getAllAccounts(AccountFilterRequest filterRequest, Pageable pageable);
    ResponseEntity<SuccessResponse> deleteAccount(UUID id);
    WrapResponse<AccountResponse> getByIdAccount(UUID id);
    ResponseEntity<SuccessResponse> updateAccount(UpdateAccountRequest request) throws IOException;
    ResponseEntity<SuccessResponse> createAccount(CreateAccountRequest request) throws IOException;
    LocationIdResponse getLocationIdByAccountId(UUID accountId);
//    WrapResponse<List<AccountStatisticResponse>> getAccountStatistics();
    WrapResponse<AccountStatisticResponse> getAccountStatistics(UUID id);
}
