package com.snap.reactive.demo.services.async;

import com.snap.reactive.demo.models.OrderSummary;

import java.util.concurrent.CompletableFuture;

public interface AsyncOrderSummaryService {
    CompletableFuture<OrderSummary> getOrderSummaryByNumber(long orderNumber);
}
