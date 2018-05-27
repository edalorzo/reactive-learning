package com.snap.reactive.demo.api.rx;

import com.snap.reactive.demo.api.models.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<Customer> getCustomerByEmail(String email);
}
