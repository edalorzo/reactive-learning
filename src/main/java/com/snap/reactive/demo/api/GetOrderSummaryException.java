package com.snap.reactive.demo.api;

public class GetOrderSummaryException extends RuntimeException {

    private long orderNumber;

    public GetOrderSummaryException(long orderNumber, Throwable cause) {
        super(cause);
        this.orderNumber = orderNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Failure to obtain order summary for order %d", orderNumber);
    }
}
