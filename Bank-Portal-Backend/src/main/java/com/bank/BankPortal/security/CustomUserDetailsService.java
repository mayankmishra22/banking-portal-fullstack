package com.bank.BankPortal.security;

import com.bank.BankPortal.entity.User;
import com.bank.BankPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        System.out.println("----------> Loading user by identifier: " + identifier);
        User user = userRepository.findByEmail(identifier)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + identifier));
        System.out.println("----------> " + user);
        return new CustomUserDetails( user);
    }

}
