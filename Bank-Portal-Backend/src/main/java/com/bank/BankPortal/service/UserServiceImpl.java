package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.UserDTO.UpdateUserRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;
import com.bank.BankPortal.entity.User;
import com.bank.BankPortal.exception.UserNotFoundException;
import com.bank.BankPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        System.out.println("Updating user: " + user.getUsername() + ", Email: " + user.getEmail());
        User updated = userRepository.save(user);
        return mapToResponse(updated);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
