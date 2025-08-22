package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.TransactionRequest;
import com.bank.BankPortal.dto.TransactionResponse;
import com.bank.BankPortal.entity.*;
import com.bank.BankPortal.exception.*;
import com.bank.BankPortal.repository.AccountRepository;
import com.bank.BankPortal.repository.TransactionRepository;
import com.bank.BankPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bank.BankPortal.service.AccountServiceImpl.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public TransactionResponse transferFunds(TransactionRequest request) {
        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        Optional<Account> fromAccount = accountRepository.findById(request.getFromAccountId());
        Optional<Account> toAccount = accountRepository.findById(request.getToAccountId());
        User user = userRepository.findByEmail(getCurrentUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username/email: " + getCurrentUsername()));

        if(toAccount.isPresent() && fromAccount.isPresent()) {
            if (!fromAccount.get().getUser().getId().equals(user.getId())) {
                throw new UnauthorizedAccessException("User with ID: " + user.getId() + " is not authorized to perform this transaction");
            }

            // Check if sender's account has sufficient balance
            Account sender = fromAccount.get();
            Account receiver = toAccount.get();
            BigDecimal previousBalance = sender.getBalance();
            BigDecimal transferAmount = request.getAmount();

            if(previousBalance.compareTo(transferAmount) < 0.0) {
                throw new InsufficientFundsException("Insufficient balance in account ID: " + request.getFromAccountId());
            }
            // Deduct amount from sender's account
            sender.setBalance(previousBalance.subtract(transferAmount));
            accountRepository.save(sender); // Save updated sender account

            BigDecimal remainingBalance = sender.getBalance();
            // Add amount to receiver's account
            receiver.setBalance(receiver.getBalance().add(transferAmount));
            accountRepository.save(toAccount.get()); // Save updated receiver account
            System.out.println("----------------------------------------------------------------------------------------");
            System.out.println(previousBalance + " " + transferAmount + " " + remainingBalance);
            System.out.println("Transfer successful from Account ID: " + request.getFromAccountId() + " to Account ID: " + request.getToAccountId());
            System.out.println("New balance for sender: " + sender.getBalance());
            System.out.println("New balance for receiver: " + receiver.getBalance());
            System.out.println("----------------------------------------------------------------------------------------");
            // Create transaction record
            Transaction transaction = Transaction.builder()
                    .fromAccount(sender)
                    .toAccount(receiver)
                    .amount(request.getAmount())
                    .type(TransactionType.DEBIT)
                    .date(LocalDate.now())
                    .timestamp(LocalDateTime.now())
                    .fromBalanceAfter(previousBalance)
                    .toBalanceAfter(remainingBalance)
                    .status(TransactionStatus.SUCCESS)
                    .build();
            transactionRepository.save(transaction); // Save transaction record
            return mapToResponse(transaction); // Map Transaction -> TransactionResponse
        }
        if(toAccount.isPresent()){
            throw new AccountNotFoundException("Account with ID: " + request.getFromAccountId() + " does not exist");
        }
        throw new AccountNotFoundException("Account with ID: " + request.getToAccountId() + " does not exist");
    }


    @Override
    public List<TransactionResponse> getTransactionHistory(Long accountId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Date cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new ValidationException("End date cannot be before start date");
        }

        List<Transaction> transactions;

        if (accountId == null) {
            transactions = transactionRepository.findByDateBetween(startDate, endDate);
        } else {
            transactions = transactionRepository.findByFromAccount_IdAndDateBetween(
                    accountId,
                    startDate,    // if fromAccount date is LocalDateTime
                    endDate
            );
        }


        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .fromAccountId(transaction.getFromAccount() != null ? transaction.getFromAccount().getId() : null)
                .toAccountId(transaction.getToAccount() != null ? transaction.getToAccount().getId() : null)
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .status("SUCCESS") // later we can switch to actual status
                .fromBalanceAfter(transaction.getFromBalanceAfter() != null ? transaction.getFromBalanceAfter() : null)
                .toBalanceAfter(transaction.getToBalanceAfter() != null ? transaction.getToBalanceAfter() : null)
                .build();
    }


}
