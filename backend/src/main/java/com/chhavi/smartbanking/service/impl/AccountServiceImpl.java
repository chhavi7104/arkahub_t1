package com.chhavi.smartbanking.service.impl;

import com.chhavi.smartbanking.dto.request.CreateAccountRequest;
import com.chhavi.smartbanking.dto.request.UpdateAccountRequest;
import com.chhavi.smartbanking.dto.response.AccountResponse;
import com.chhavi.smartbanking.entity.Account;
import com.chhavi.smartbanking.entity.AccountStatus;
import com.chhavi.smartbanking.exception.AccountNotFoundException;
import com.chhavi.smartbanking.exception.DuplicateAccountException;
import com.chhavi.smartbanking.repository.AccountRepository;
import com.chhavi.smartbanking.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getInitialBalance());
        account.setStatus(AccountStatus.ACTIVE);

        return mapToResponse(accountRepository.save(account));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        return mapToResponse(findAccountOrThrow(accountNumber));
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(String accountNumber, UpdateAccountRequest request) {
        Account account = findAccountOrThrow(accountNumber);
        account.setAccountHolderName(request.getAccountHolderName());
        account.setStatus(request.getStatus());
        return mapToResponse(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        Account account = findAccountOrThrow(accountNumber);
        accountRepository.delete(account);
    }

    private Account findAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with account number: " + accountNumber));
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        int attempts = 0;
        do {
            if (++attempts > 10) {
                throw new DuplicateAccountException("Unable to generate a unique account number, please retry");
            }
            long random = ThreadLocalRandom.current().nextLong(1_000_000_000L);
            accountNumber = "ACC" + String.format("%09d", random);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
                account.getId(), account.getAccountNumber(), account.getAccountHolderName(),
                account.getBalance(), account.getStatus(), account.getCreatedAt());
    }
}