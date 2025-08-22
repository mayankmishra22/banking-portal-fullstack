package com.bank.BankPortal.controller;

import com.bank.BankPortal.dto.AuthResponse;
import com.bank.BankPortal.dto.LogoutRequest;
import com.bank.BankPortal.dto.RefreshRequest;
import com.bank.BankPortal.dto.UserDTO.UserLoginRequest;
import com.bank.BankPortal.dto.UserDTO.UserRegistrationRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;
import com.bank.BankPortal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}

