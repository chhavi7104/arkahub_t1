package com.chhavi.smartbanking.service.impl;

import com.chhavi.smartbanking.dto.request.CreateAccountRequest;
import com.chhavi.smartbanking.dto.response.AccountResponse;
import com.chhavi.smartbanking.entity.Account;
import com.chhavi.smartbanking.entity.AccountStatus;
import com.chhavi.smartbanking.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount_shouldPersistAccountWithActiveStatusAndGeneratedNumber() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountHolderName("Priya Sharma");
        request.setInitialBalance(new BigDecimal("5000.00"));

        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account acc = invocation.getArgument(0);
            acc.setId(1L);
            return acc;
        });

        AccountResponse response = accountService.createAccount(request);

        assertThat(response.getAccountHolderName()).isEqualTo("Priya Sharma");
        assertThat(response.getBalance()).isEqualByComparingTo("5000.00");
        assertThat(response.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(response.getAccountNumber()).startsWith("ACC");

        verify(accountRepository).save(any(Account.class));
    }
}