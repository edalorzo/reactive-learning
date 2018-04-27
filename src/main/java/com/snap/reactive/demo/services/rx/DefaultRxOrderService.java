package com.snap.reactive.demo.services.rx;

import com.snap.reactive.demo.models.Order;
import com.snap.reactive.demo.services.GetOrderException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
class DefaultRxOrderService implements RxOrderService {

    private final WebClient http;

    DefaultRxOrderService() {
        this.http = WebClient.create("http://localhost:4040");
    }

    @Override
    public Mono<Order> getOrderByNumber(long orderNumber) {
        String endpoint = "/orders/{orderNumber}";
        return this.http.method(HttpMethod.GET)
                        .uri(endpoint, orderNumber)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Order.class)
                        .onErrorMap(error -> new GetOrderException(orderNumber, error));
    }


}
