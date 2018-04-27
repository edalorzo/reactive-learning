package com.snap.reactive.demo.services.rx;

import com.snap.reactive.demo.models.OrderSummary;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface RxOrderSummaryService {
    Mono<OrderSummary> getOrderSummaryByNumber(long orderNumber);
}
