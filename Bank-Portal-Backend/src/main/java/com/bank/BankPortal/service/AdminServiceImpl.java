package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.AccountResponse;
import com.bank.BankPortal.dto.AdminUserResponse;
import com.bank.BankPortal.dto.TransactionResponse;
import com.bank.BankPortal.dto.UserDTO.UpdateUserRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;
import com.bank.BankPortal.entity.TransactionStatus;
import com.bank.BankPortal.entity.User;
import com.bank.BankPortal.exception.UserNotFoundException;
import com.bank.BankPortal.repository.AccountRepository;
import com.bank.BankPortal.repository.TransactionRepository;
import com.bank.BankPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new AdminUserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public UserResponse updateUserById(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    @Override
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAll().stream()
                .map(account -> new AccountResponse(account.getId(), account.getAccountNumber(), account.getType().name(), account.getBalance(), currentUser.getUsername(), currentUser.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactions(String status) {
            TransactionStatus transactionStatus = TransactionStatus.valueOf(status.toUpperCase());
            return transactionRepository.findByStatus(transactionStatus).stream()
                    .map(tx -> new TransactionResponse(tx.getId(), tx.getFromAccount().getId(), tx.getToAccount().getId(), tx.getAmount(), tx.getTimestamp(), tx.getStatus().name(), tx.getFromBalanceAfter(), tx.getToBalanceAfter()))
                    .collect(Collectors.toList());
    }
}
