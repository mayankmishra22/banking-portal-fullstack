package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.UserDTO.UpdateUserRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getCurrentUser();   // 👈 new
    UserResponse updateCurrentUser(UpdateUserRequest request); // 👈 new
}
