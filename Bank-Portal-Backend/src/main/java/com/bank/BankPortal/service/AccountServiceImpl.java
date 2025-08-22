package com.bank.BankPortal.service;

import com.bank.BankPortal.dto.AccountRequest;
import com.bank.BankPortal.dto.AccountResponse;
import com.bank.BankPortal.entity.Account;
import com.bank.BankPortal.entity.User;
import com.bank.BankPortal.exception.AccountAlreadyExistException;
import com.bank.BankPortal.exception.AccountNotFoundException;
import com.bank.BankPortal.exception.UnauthorizedAccessException;
import com.bank.BankPortal.exception.UserNotFoundException;
import com.bank.BankPortal.repository.AccountRepository;
import com.bank.BankPortal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        if (accountRepository.existsByAccountNumber(accountRequest.getAccountNumber())) {
            throw new AccountAlreadyExistException(
                    "Account with account number: " + accountRequest.getAccountNumber() + " already exists");
        }
        System.out.println("Creating account for user: " + getCurrentUsername());
        User user = userRepository.findByEmail(getCurrentUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username/email: " + getCurrentUsername()));
        Account account = Account.builder()
                .user(user)
                .accountNumber(accountRequest.getAccountNumber())
                .balance(accountRequest.getBalance())
                .type(accountRequest.getType())
                .build();

        Account saved = accountRepository.save(account);

        return new AccountResponse(saved.getId(), saved.getAccountNumber(), saved.getType().name(), saved.getBalance(), user.getUsername(), user.getId());
    }

    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Current principal: " + principal.toString());
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString(); // fallback
    }


    @Override
    public List<AccountResponse> getUserAccounts() {
        String username = getCurrentUsername();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));

        return user.getAccounts()
                .stream()
                .map(acc -> new AccountResponse(
                        acc.getId(),
                        acc.getAccountNumber(),
                        acc.getType().name(),
                        acc.getBalance(),
                        user.getUsername(),
                        user.getId()))
                .toList();
    }

    @Override
    public AccountResponse getAccountById(Long accountId) {
        if(!accountRepository.existsById(accountId)){
            throw new AccountNotFoundException("Account not found with id " + accountId);
        }
        String username = getCurrentUsername(); // get logged-in username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        // Find the account belonging to this user
        Account userAccount = user.getAccounts().stream()
                .filter(account -> Objects.equals(account.getId(), accountId))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedAccessException("You do not have access to other's account, Only Admin has Access!"));

        return new AccountResponse(
                userAccount.getId(),
                userAccount.getAccountNumber(),
                userAccount.getType().name(),
                userAccount.getBalance(),
                userAccount.getUser().getUsername(),
                userAccount.getUser().getId()
        );
    }



    @Override
    public BigDecimal getBalance(Long id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + id));
        return acc.getBalance();
    }

}
