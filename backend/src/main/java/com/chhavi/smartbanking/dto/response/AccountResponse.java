package com.chhavi.smartbanking.dto.response;

import com.chhavi.smartbanking.entity.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {

    private Long id;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createdAt;

    public AccountResponse(Long id, String accountNumber, String accountHolderName,
                            BigDecimal balance, AccountStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
