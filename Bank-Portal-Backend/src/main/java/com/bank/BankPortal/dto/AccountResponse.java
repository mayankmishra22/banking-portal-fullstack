package com.bank.BankPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountResponse {
    private Long accountId;
    private String accountNumber;
    private String accountType;   // e.g., SAVINGS, CHECKING
    private BigDecimal balance;


    private String accountHolderName; // e.g., John Doe
    private Long userId;             // ID of the user who owns the account
}

