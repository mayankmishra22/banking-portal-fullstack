package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.AccountRequest;
import com.bank.BankPortal.dto.AccountResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<AccountResponse> getUserAccounts();
    AccountResponse getAccountById(Long accountId);

    BigDecimal getBalance(Long id);

    AccountResponse createAccount(AccountRequest accountRequest);
}
