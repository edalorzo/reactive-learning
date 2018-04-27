package com.snap.reactive.demo.services.rx;

import com.snap.reactive.demo.models.Customer;
import com.snap.reactive.demo.models.Order;
import com.snap.reactive.demo.services.GetCustomerException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
class DefaultRxCustomerService implements RxCustomerService {

    private final WebClient http;

    DefaultRxCustomerService () {
        this.http = WebClient.create("http://localhost:4040");
    }

    @Override
    public Mono<Customer> getCustomerByEmail(String email) {
        String endpoint = "/customer/{customerEmail}";
        return this.http.method(HttpMethod.GET)
                        .uri(endpoint, email)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Customer.class)
                        .onErrorMap(error -> new GetCustomerException(email, error));
    }
}
