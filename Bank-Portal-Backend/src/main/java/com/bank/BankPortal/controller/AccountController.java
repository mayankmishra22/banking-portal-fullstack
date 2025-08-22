package com.bank.BankPortal.controller;

import com.bank.BankPortal.dto.AccountRequest;
import com.bank.BankPortal.dto.AccountResponse;
import com.bank.BankPortal.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts() {
        return ResponseEntity.ok(accountService.getUserAccounts());
    }

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.status(201).body(accountService.createAccount(accountRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }
}

