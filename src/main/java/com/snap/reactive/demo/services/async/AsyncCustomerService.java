package com.snap.reactive.demo.services.async;

import com.snap.reactive.demo.models.Customer;

import java.util.concurrent.CompletableFuture;

public interface AsyncCustomerService {
    CompletableFuture<Customer> getCustomerByEmail(String email);
}
