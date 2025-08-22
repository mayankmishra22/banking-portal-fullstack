package com.bank.BankPortal.controller;

import com.bank.BankPortal.dto.TransactionRequest;
import com.bank.BankPortal.dto.TransactionResponse;
import com.bank.BankPortal.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse tx = transactionService.transferFunds(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tx);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponse>> history(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        System.out.println("Fetching transaction history for accountId: " + accountId + ", from: " + from + ", to: " + to);
        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (from != null) fromDate = LocalDate.parse(from);
        if (to != null) toDate = LocalDate.parse(to);
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId, fromDate, toDate));
    }

}
