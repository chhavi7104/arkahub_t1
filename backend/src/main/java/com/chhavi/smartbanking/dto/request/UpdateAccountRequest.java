package com.chhavi.smartbanking.dto.request;

import com.chhavi.smartbanking.entity.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateAccountRequest {

    @NotBlank(message = "Account holder name is required")
    @Size(min = 3, max = 100, message = "Account holder name must be between 3 and 100 characters")
    private String accountHolderName;

    @NotNull(message = "Status is required")
    private AccountStatus status;

    public UpdateAccountRequest() {}

    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
}