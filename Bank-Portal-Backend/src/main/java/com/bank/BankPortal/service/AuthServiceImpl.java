package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.*;
import com.bank.BankPortal.dto.UserDTO.UserLoginRequest;
import com.bank.BankPortal.dto.UserDTO.UserRegistrationRequest;
import com.bank.BankPortal.dto.UserDTO.UserResponse;
import com.bank.BankPortal.entity.User;
import com.bank.BankPortal.exception.AccountAlreadyExistException;
import com.bank.BankPortal.exception.UnauthorizedAccessException;
import com.bank.BankPortal.exception.UserNotFoundException;
import com.bank.BankPortal.repository.UserRepository;
import com.bank.BankPortal.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(UserRegistrationRequest request) {
//        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
//            throw new AccountAlreadyExistException("Username already exists");
//        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AccountAlreadyExistException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        String accessToken = jwtService.generateToken(savedUser, false);  // short-lived
        String refreshToken = jwtService.generateToken(savedUser, true);  // long-lived

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username/email: " + request.getUsername())));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedAccessException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(user, false);  // short-lived
        String refreshToken = jwtService.generateToken(user, true);  // long-lived

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new UnauthorizedAccessException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        String newAccessToken = jwtService.generateToken(user, false);

        return new AuthResponse(newAccessToken, refreshToken);
    }

    @Override
    public void logout(LogoutRequest request) {
        // ⚡ Option 1: Stateless JWT → no real logout (client just drops token)
        // ⚡ Option 2: Maintain blacklist of invalidated tokens (Redis/DB)

        // For now, we’ll assume stateless: do nothing
        System.out.println("Token invalidated: " + request.getToken());
    }
}
