package com.snap.reactive.demo.services;

public class GetOrderException extends RuntimeException {
    private final long orderNumber;

    public GetOrderException(long orderNumber, Throwable cause) {
        super(cause);
        this.orderNumber = orderNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Failure while trying to retrieve order information: %s", orderNumber);
    }
}
