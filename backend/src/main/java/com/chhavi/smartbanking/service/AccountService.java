package com.chhavi.smartbanking.service;

import com.chhavi.smartbanking.dto.request.CreateAccountRequest;
import com.chhavi.smartbanking.dto.request.UpdateAccountRequest;
import com.chhavi.smartbanking.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);
    List<AccountResponse> getAllAccounts();
    AccountResponse getAccountByAccountNumber(String accountNumber);
    AccountResponse updateAccount(String accountNumber, UpdateAccountRequest request);
    void deleteAccount(String accountNumber);
}