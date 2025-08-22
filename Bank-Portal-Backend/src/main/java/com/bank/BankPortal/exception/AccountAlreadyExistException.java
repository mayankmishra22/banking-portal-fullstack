package com.bank.BankPortal.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
