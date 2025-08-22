package com.bank.BankPortal.controller;

import com.bank.BankPortal.dto.UserDTO.UpdateUserRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;
import com.bank.BankPortal.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }
}
