package com.snap.reactive.demo.services.rx;

import com.snap.reactive.demo.models.Order;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface RxOrderService {
    Mono<Order> getOrderByNumber(long orderNumber);
}
