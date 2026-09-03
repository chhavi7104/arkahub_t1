package com.chhavi.smartbanking.service.impl;

import com.chhavi.smartbanking.dto.request.DepositWithdrawRequest;
import com.chhavi.smartbanking.dto.request.TransferRequest;
import com.chhavi.smartbanking.dto.response.TransactionResponse;
import com.chhavi.smartbanking.entity.*;
import com.chhavi.smartbanking.exception.*;
import com.chhavi.smartbanking.repository.AccountRepository;
import com.chhavi.smartbanking.repository.TransactionRepository;
import com.chhavi.smartbanking.service.TransactionAuditService;
import com.chhavi.smartbanking.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionAuditService transactionAuditService;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                   AccountRepository accountRepository,
                                   TransactionAuditService transactionAuditService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionAuditService = transactionAuditService;
    }

    @Override
    @Transactional
    public TransactionResponse deposit(DepositWithdrawRequest request) {
        Account account = getActiveAccountOrThrow(request.getAccountNumber());

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction txn = buildTransaction(null, account.getAccountNumber(),
                request.getAmount(), TransactionType.DEPOSIT, TransactionStatus.SUCCESS);
        return mapToResponse(transactionRepository.save(txn));
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(DepositWithdrawRequest request) {
        Account account = getActiveAccountOrThrow(request.getAccountNumber());

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            Transaction failed = buildTransaction(account.getAccountNumber(), null,
                    request.getAmount(), TransactionType.WITHDRAW, TransactionStatus.FAILED);
            transactionAuditService.logFailedTransaction(failed);
            throw new InsufficientBalanceException(
                    "Insufficient balance in account: " + account.getAccountNumber());
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction txn = buildTransaction(account.getAccountNumber(), null,
                request.getAmount(), TransactionType.WITHDRAW, TransactionStatus.SUCCESS);
        return mapToResponse(transactionRepository.save(txn));
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request.getSenderAccountNumber().equals(request.getReceiverAccountNumber())) {
            throw new InvalidTransactionException("Sender and receiver account cannot be the same");
        }

        Account sender = getActiveAccountOrThrow(request.getSenderAccountNumber());
        Account receiver = getActiveAccountOrThrow(request.getReceiverAccountNumber());

        if (sender.getBalance().compareTo(request.getAmount()) < 0) {
            Transaction failed = buildTransaction(sender.getAccountNumber(), receiver.getAccountNumber(),
                    request.getAmount(), TransactionType.TRANSFER, TransactionStatus.FAILED);
            transactionAuditService.logFailedTransaction(failed);
            throw new InsufficientBalanceException(
                    "Insufficient balance in sender account: " + sender.getAccountNumber());
        }

        // Debit and credit happen inside the same @Transactional method — if the
        // process dies or an exception is thrown after the debit but before the
        // credit, Spring rolls back both changes together. Neither write is
        // flushed to the DB until the transaction commits.
        sender.setBalance(sender.getBalance().subtract(request.getAmount()));
        receiver.setBalance(receiver.getBalance().add(request.getAmount()));
        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction txn = buildTransaction(sender.getAccountNumber(), receiver.getAccountNumber(),
                request.getAmount(), TransactionType.TRANSFER, TransactionStatus.SUCCESS);
        return mapToResponse(transactionRepository.save(txn));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByTransactionId(String transactionId) {
        Transaction txn = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction not found with id: " + transactionId));
        return mapToResponse(txn);
    }

    private Account getActiveAccountOrThrow(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with account number: " + accountNumber));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(
                    "Account " + accountNumber + " is " + account.getStatus() +
                            " and cannot perform transactions");
        }
        return account;
    }

    private Transaction buildTransaction(String from, String to, BigDecimal amount,
                                          TransactionType type, TransactionStatus status) {
        Transaction txn = new Transaction();
        txn.setTransactionId(generateUniqueTransactionId());
        txn.setFromAccount(from);
        txn.setToAccount(to);
        txn.setAmount(amount);
        txn.setTransactionType(type);
        txn.setStatus(status);
        return txn;
    }

    private String generateUniqueTransactionId() {
        String transactionId;
        do {
            transactionId = "TXN" + UUID.randomUUID().toString().replace("-", "")
                    .substring(0, 12).toUpperCase();
        } while (transactionRepository.existsByTransactionId(transactionId));
        return transactionId;
    }

    private TransactionResponse mapToResponse(Transaction txn) {
        return new TransactionResponse(
                txn.getId(), txn.getTransactionId(), txn.getFromAccount(), txn.getToAccount(),
                txn.getAmount(), txn.getTransactionType(), txn.getStatus(), txn.getCreatedAt());
    }
}