package com.snap.reactive.demo.api.async;

import com.snap.reactive.demo.api.models.OrderSummary;

import java.util.concurrent.CompletableFuture;

public interface OrderSummaryService {
    CompletableFuture<OrderSummary> getOrderSummaryByNumber(long orderNumber);
}
