package com.snap.reactive.demo.api.rx;

import com.snap.reactive.demo.api.models.OrderSummary;
import reactor.core.publisher.Mono;

public interface OrderSummaryService {
    Mono<OrderSummary> getOrderSummaryByNumber(long orderNumber);
}
