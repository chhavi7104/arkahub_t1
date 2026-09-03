package com.chhavi.smartbanking.service;

import com.chhavi.smartbanking.dto.request.DepositWithdrawRequest;
import com.chhavi.smartbanking.dto.request.TransferRequest;
import com.chhavi.smartbanking.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse deposit(DepositWithdrawRequest request);
    TransactionResponse withdraw(DepositWithdrawRequest request);
    TransactionResponse transfer(TransferRequest request);
    Page<TransactionResponse> getAllTransactions(Pageable pageable);
    TransactionResponse getTransactionByTransactionId(String transactionId);
}