package com.bank.BankPortal.controller;

import com.bank.BankPortal.dto.AccountResponse;
import com.bank.BankPortal.dto.AdminUserResponse;
import com.bank.BankPortal.dto.TransactionResponse;
import com.bank.BankPortal.dto.UserDTO.UpdateUserRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;
import com.bank.BankPortal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> listUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUserById(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(adminService.updateUserById(id, request));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> listAccounts() {
        return ResponseEntity.ok(adminService.getAllAccounts());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> monitorTransactions(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(adminService.getTransactions(status));
    }
}
