package com.chhavi.smartbanking;

import com.chhavi.smartbanking.dto.request.TransferRequest;
import com.chhavi.smartbanking.entity.Account;
import com.chhavi.smartbanking.entity.AccountStatus;
import com.chhavi.smartbanking.exception.InsufficientBalanceException;
import com.chhavi.smartbanking.repository.AccountRepository;
import com.chhavi.smartbanking.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class TransferTransactionalIntegrationTest {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void failedTransfer_shouldNotChangeEitherAccountBalance_inTheRealDatabase() {
        Account sender = new Account();
        sender.setAccountNumber("ACC111111111");
        sender.setAccountHolderName("Sender");
        sender.setBalance(new BigDecimal("100.00"));
        sender.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(sender);

        Account receiver = new Account();
        receiver.setAccountNumber("ACC222222222");
        receiver.setAccountHolderName("Receiver");
        receiver.setBalance(new BigDecimal("50.00"));
        receiver.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(receiver);

        TransferRequest request = new TransferRequest();
        request.setSenderAccountNumber("ACC111111111");
        request.setReceiverAccountNumber("ACC222222222");
        request.setAmount(new BigDecimal("10000.00")); // exceeds sender's balance

        assertThatThrownBy(() -> transactionService.transfer(request))
                .isInstanceOf(InsufficientBalanceException.class);

        // Re-fetch from the actual database (not the in-memory Java objects
        // above) to prove the failed transfer left zero trace on either balance.
        Account senderAfter = accountRepository.findByAccountNumber("ACC111111111").orElseThrow();
        Account receiverAfter = accountRepository.findByAccountNumber("ACC222222222").orElseThrow();

        assertThat(senderAfter.getBalance()).isEqualByComparingTo("100.00");
        assertThat(receiverAfter.getBalance()).isEqualByComparingTo("50.00");
    }
}