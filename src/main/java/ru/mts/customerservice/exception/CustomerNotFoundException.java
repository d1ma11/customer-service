package ru.mts.customerservice.exception;

public class CustomerNotFoundException extends CustomException {

    public CustomerNotFoundException(String code, String message) {
        super(code, message);
    }
}
