package com.snap.reactive.demo.services.rx;

import com.snap.reactive.demo.models.Customer;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface RxCustomerService {
    Mono<Customer> getCustomerByEmail(String email);
}
