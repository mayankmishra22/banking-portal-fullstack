package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.AccountResponse;
import com.bank.BankPortal.dto.AdminUserResponse;
import com.bank.BankPortal.dto.TransactionResponse;
import com.bank.BankPortal.dto.UserDTO.UpdateUserRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;

import java.util.List;

public interface AdminService {
    List<AdminUserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    UserResponse updateUserById(Long id, UpdateUserRequest request);
    void deleteUserById(Long id);
    List<AccountResponse> getAllAccounts();
    List<TransactionResponse> getTransactions(String status);
}
