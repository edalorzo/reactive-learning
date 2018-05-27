package com.snap.reactive.demo.api.async;

import com.snap.reactive.demo.api.models.Customer;

import java.util.concurrent.CompletableFuture;

public interface CustomerService {
    CompletableFuture<Customer> getCustomerByEmail(String email);
}
