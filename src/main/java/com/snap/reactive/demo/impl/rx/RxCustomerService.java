package com.snap.reactive.demo.impl.rx;

import com.snap.reactive.demo.api.GetCustomerException;
import com.snap.reactive.demo.api.models.Customer;
import com.snap.reactive.demo.api.rx.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
class RxCustomerService implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient http;

    @Autowired
    RxCustomerService(WebClient http) {
        this.http = http;
    }

    @Override
    public Mono<Customer> getCustomerByEmail(String email) {
        String endpoint = "/customer/{customerEmail}";
        return this.http.method(HttpMethod.GET)
                        .uri(endpoint, email)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Customer.class)
                        .doOnNext(customer -> logger.info("Found customer for email {}: {}", email, customer))
                        .onErrorMap(error -> new GetCustomerException(email, error));
    }
}
