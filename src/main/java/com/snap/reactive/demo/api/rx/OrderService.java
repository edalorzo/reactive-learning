package com.snap.reactive.demo.api.rx;

import com.snap.reactive.demo.api.models.Order;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<Order> getOrderByNumber(long orderNumber);
}
