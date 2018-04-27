package com.snap.reactive.demo.services;

public class GetCustomerException extends RuntimeException {

    private final String customerEmail;

    public GetCustomerException(String customerEmail, Throwable cause) {
        super(cause);
        this.customerEmail = customerEmail;
    }

    @Override
    public String getMessage() {
        return String.format("Failure while trying to retrieve customer information: %s", customerEmail);
    }

}
