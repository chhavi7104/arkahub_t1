package com.chhavi.smartbanking.service.impl;

import com.chhavi.smartbanking.dto.request.DepositWithdrawRequest;
import com.chhavi.smartbanking.dto.request.TransferRequest;
import com.chhavi.smartbanking.dto.response.TransactionResponse;
import com.chhavi.smartbanking.entity.Account;
import com.chhavi.smartbanking.entity.AccountStatus;
import com.chhavi.smartbanking.exception.InsufficientBalanceException;
import com.chhavi.smartbanking.repository.AccountRepository;
import com.chhavi.smartbanking.repository.TransactionRepository;
import com.chhavi.smartbanking.service.TransactionAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private TransactionAuditService transactionAuditService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account senderAccount;
    private Account receiverAccount;

    @BeforeEach
    void setUp() {
        senderAccount = new Account();
        senderAccount.setId(1L);
        senderAccount.setAccountNumber("ACC000000001");
        senderAccount.setAccountHolderName("Sender");
        senderAccount.setBalance(new BigDecimal("1000.00"));
        senderAccount.setStatus(AccountStatus.ACTIVE);

        receiverAccount = new Account();
        receiverAccount.setId(2L);
        receiverAccount.setAccountNumber("ACC000000002");
        receiverAccount.setAccountHolderName("Receiver");
        receiverAccount.setBalance(new BigDecimal("500.00"));
        receiverAccount.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    void deposit_shouldIncreaseBalanceAndReturnSuccessTransaction() {
        DepositWithdrawRequest request = new DepositWithdrawRequest();
        request.setAccountNumber(senderAccount.getAccountNumber());
        request.setAmount(new BigDecimal("200.00"));

        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber()))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TransactionResponse response = transactionService.deposit(request);

        assertThat(senderAccount.getBalance()).isEqualByComparingTo("1200.00");
        assertThat(response.getStatus().toString()).isEqualTo("SUCCESS");
    }

    @Test
    void withdraw_shouldDecreaseBalance_whenSufficientFunds() {
        DepositWithdrawRequest request = new DepositWithdrawRequest();
        request.setAccountNumber(senderAccount.getAccountNumber());
        request.setAmount(new BigDecimal("300.00"));

        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber()))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        transactionService.withdraw(request);

        assertThat(senderAccount.getBalance()).isEqualByComparingTo("700.00");
    }

    @Test
    void withdraw_shouldThrow_whenInsufficientBalance() {
        DepositWithdrawRequest request = new DepositWithdrawRequest();
        request.setAccountNumber(senderAccount.getAccountNumber());
        request.setAmount(new BigDecimal("5000.00"));

        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber()))
                .thenReturn(Optional.of(senderAccount));

        assertThatThrownBy(() -> transactionService.withdraw(request))
                .isInstanceOf(InsufficientBalanceException.class);

        assertThat(senderAccount.getBalance()).isEqualByComparingTo("1000.00");
        verify(transactionAuditService).logFailedTransaction(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_shouldMoveFunds_whenSenderHasSufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setSenderAccountNumber(senderAccount.getAccountNumber());
        request.setReceiverAccountNumber(receiverAccount.getAccountNumber());
        request.setAmount(new BigDecimal("400.00"));

        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber()))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber(receiverAccount.getAccountNumber()))
                .thenReturn(Optional.of(receiverAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));
        when(transactionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        transactionService.transfer(request);

        assertThat(senderAccount.getBalance()).isEqualByComparingTo("600.00");
        assertThat(receiverAccount.getBalance()).isEqualByComparingTo("900.00");
    }

    @Test
    void transfer_shouldFailAndLeaveBalancesUnchanged_whenSenderHasInsufficientBalance() {
        TransferRequest request = new TransferRequest();
        request.setSenderAccountNumber(senderAccount.getAccountNumber());
        request.setReceiverAccountNumber(receiverAccount.getAccountNumber());
        request.setAmount(new BigDecimal("999999.00"));

        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber()))
                .thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber(receiverAccount.getAccountNumber()))
                .thenReturn(Optional.of(receiverAccount));

        assertThatThrownBy(() -> transactionService.transfer(request))
                .isInstanceOf(InsufficientBalanceException.class);

        // Critical assertion: neither balance moved.
        assertThat(senderAccount.getBalance()).isEqualByComparingTo("1000.00");
        assertThat(receiverAccount.getBalance()).isEqualByComparingTo("500.00");
        verify(accountRepository, never()).save(any());
    }
}