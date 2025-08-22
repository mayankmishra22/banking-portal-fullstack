package com.bank.BankPortal.repository;

import com.bank.BankPortal.entity.Transaction;
import com.bank.BankPortal.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find all transactions where the account is sender
    List<Transaction> findByFromAccount_Id(Long accountId);

    // Find all transactions where the account is receiver
    List<Transaction> findByToAccount_Id(Long accountId);

    // Find all transactions where the account is sender in a date range
    List<Transaction> findByFromAccount_IdAndDateBetween(Long accountId, LocalDate startDate, LocalDate endDate);

    // Find all transactions where the account is receiver in a date range
    List<Transaction> findByToAccount_IdAndDateBetween(Long accountId, LocalDate startDate, LocalDate endDate);

    // Find all transactions by status
    List<Transaction> findByStatus(TransactionStatus status);

    // Find all transactions in a date range (for all accounts)
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
