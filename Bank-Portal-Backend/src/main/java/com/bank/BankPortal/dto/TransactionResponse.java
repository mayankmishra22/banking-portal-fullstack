package com.bank.BankPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TransactionResponse {
    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String status;

    // new fields for tracking balances
    private BigDecimal fromBalanceAfter;
    private BigDecimal toBalanceAfter;
}
