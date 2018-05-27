package com.snap.reactive.demo.api.async;

import com.snap.reactive.demo.api.models.Order;

import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<Order> getOrderByNumber(long orderNumber);
}
