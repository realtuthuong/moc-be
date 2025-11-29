package com.example.MocBE.service;

import com.example.MocBE.dto.request.CustomerOrderReservationRequest;

public interface PendingOrderService {
    void savePending(String txnRef, CustomerOrderReservationRequest request);
    CustomerOrderReservationRequest getPendingByTxnRef(String txnRef);
    void deletePending(String txnRef);
}
