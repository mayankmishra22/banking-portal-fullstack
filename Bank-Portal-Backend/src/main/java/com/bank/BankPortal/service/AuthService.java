package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.*;
import com.bank.BankPortal.dto.UserDTO.UserLoginRequest;
import com.bank.BankPortal.dto.UserDTO.UserRegistrationRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;

public interface AuthService {
    AuthResponse register(UserRegistrationRequest request);
    AuthResponse login(UserLoginRequest request);
    AuthResponse refreshToken(RefreshRequest request);
    void logout(LogoutRequest request);
}

