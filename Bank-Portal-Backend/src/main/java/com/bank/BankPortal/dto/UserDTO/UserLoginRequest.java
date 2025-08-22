package com.bank.BankPortal.dto.UserDTO;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}