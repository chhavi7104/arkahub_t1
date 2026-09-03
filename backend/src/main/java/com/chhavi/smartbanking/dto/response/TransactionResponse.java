package com.chhavi.smartbanking.dto.response;

import com.chhavi.smartbanking.entity.TransactionStatus;
import com.chhavi.smartbanking.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public TransactionResponse(Long id, String transactionId, String fromAccount, String toAccount,
                                BigDecimal amount, TransactionType transactionType,
                                TransactionStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getFromAccount() { return fromAccount; }
    public String getToAccount() { return toAccount; }
    public BigDecimal getAmount() { return amount; }
    public TransactionType getTransactionType() { return transactionType; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}