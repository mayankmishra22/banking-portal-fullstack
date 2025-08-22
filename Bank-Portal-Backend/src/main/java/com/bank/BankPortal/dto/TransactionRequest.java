package com.bank.BankPortal.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}