package com.bank.BankPortal.dto.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "Invalid email format")
    private String email;
}
