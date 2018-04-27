package com.snap.reactive.demo.services.async;


import com.snap.reactive.demo.models.Customer;
import com.snap.reactive.demo.services.GetCustomerException;
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
class DefaultAsyncCustomerService implements AsyncCustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncRestTemplate restTemplate;
    //private final ExecutorService executorService;

    @Autowired
    DefaultAsyncCustomerService(AsyncRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        //this.executorService = executorService;
    }

//    @Override
//    public CompletableFuture<Customer> getCustomerByEmail(String email) {
//        requireNonNull(email, "The email must not be null");
//        return CompletableFuture.supplyAsync(() -> performRequest(email), executorService)
//                .handle(getCustomerOrElseFail(email));
//    }

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
