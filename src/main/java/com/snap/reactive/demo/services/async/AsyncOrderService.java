package com.snap.reactive.demo.services.async;

import com.snap.reactive.demo.models.Order;

import java.util.concurrent.CompletableFuture;

public interface AsyncOrderService {
    CompletableFuture<Order> getOrderByNumber(long orderNumber);
}
