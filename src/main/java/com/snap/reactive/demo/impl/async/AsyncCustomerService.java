package com.snap.reactive.demo.impl.async;


import com.snap.reactive.demo.api.GetCustomerException;
import com.snap.reactive.demo.api.async.CustomerService;
import com.snap.reactive.demo.api.models.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

@Service
class AsyncCustomerService implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncRestTemplate restTemplate;

    @Autowired
    AsyncCustomerService(AsyncRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }



    @Override
    public CompletableFuture<Customer> getCustomerByEmail(String email) {
        requireNonNull(email, "The email must not be null");
        return performRequest(email).handle(getCustomerOrElseFail(email));
    }

    private BiFunction<Customer, Throwable, Customer> getCustomerOrElseFail(String email) {
        return (customer, error) -> {
            if(error != null) {
                throw new GetCustomerException(email, error);
            }
            logger.info("Found customer for email {}: {}", email, customer);
            return customer;
        };
    }

    private CompletableFuture<Customer> performRequest(String email) {
        String endpoint = "http://localhost:4040/customer/{customerEmail}";
        ListenableFuture<ResponseEntity<Customer>> future = restTemplate.getForEntity(endpoint, Customer.class, email);
        return future.completable().thenApply(ResponseEntity::getBody);
    }

}
