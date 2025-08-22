package com.bank.BankPortal.dto;

import com.bank.BankPortal.entity.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotNull(message = "Account number is required")
    @Size(min = 5, max = 20, message = "Account number must be between 5 and 20 characters (Starting from : 10000)")
    private String accountNumber;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be positive or zero")
    private BigDecimal balance;

    @NotNull(message = "Account type is required")
    private AccountType type;
}
