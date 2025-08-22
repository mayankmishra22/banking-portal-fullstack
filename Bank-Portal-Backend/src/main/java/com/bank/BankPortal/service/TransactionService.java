package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.TransactionRequest;
import com.bank.BankPortal.dto.TransactionResponse;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionResponse transferFunds(TransactionRequest request);
    List<TransactionResponse> getTransactionHistory(Long accountId, LocalDate startDate, LocalDate endDate);
}
