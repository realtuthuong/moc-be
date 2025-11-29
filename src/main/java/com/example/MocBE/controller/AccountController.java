package com.example.MocBE.controller;

import com.example.MocBE.dto.request.AccountFilterRequest;
import com.example.MocBE.dto.request.CreateAccountRequest;
import com.example.MocBE.dto.request.UpdateAccountRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public WrapResponse<PageResponse<AccountResponse>> getAccounts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            AccountFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return accountService.getAllAccounts(filterRequest, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse>  deleteAccount(@PathVariable UUID id) {
        return accountService.deleteAccount(id);
    }

    @GetMapping("/{id}")
    public WrapResponse<AccountResponse> getById(@PathVariable UUID id) {
        return accountService.getByIdAccount(id);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateAccount(@ModelAttribute UpdateAccountRequest request) throws IOException {
        return accountService.updateAccount(request);
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> createAccount(@Valid @ModelAttribute CreateAccountRequest request) throws IOException {
        return accountService.createAccount(request);
    }
    @GetMapping("/{id}/location")
    public ResponseEntity<LocationIdResponse> getLocationIdByAccountId(@PathVariable UUID id) {
        LocationIdResponse response = accountService.getLocationIdByAccountId(id);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/statistics")
//    public WrapResponse<List<AccountStatisticResponse>> getAccountStatistics() {
//        return accountService.getAccountStatistics();
//    }

    @GetMapping("/{id}/statistics")
    public WrapResponse<AccountStatisticResponse> getAccountStatistics(@PathVariable UUID id) {
        return accountService.getAccountStatistics(id);
    }

}