package com.bank.BankPortal.dto.UserDTO;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;  // plain text only during registration
}
