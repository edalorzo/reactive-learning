package com.snap.reactive.demo.impl.rx;

import com.snap.reactive.demo.api.GetOrderException;
import com.snap.reactive.demo.api.models.Order;
import com.snap.reactive.demo.api.rx.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
class RxOrderService implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WebClient http;

    @Autowired
    RxOrderService(WebClient http) {
        this.http = http;
    }

    @Override
    public Mono<Order> getOrderByNumber(long orderNumber) {
        String endpoint = "/orders/{orderNumber}";
        return this.http.method(HttpMethod.GET)
                        .uri(endpoint, orderNumber)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Order.class)
                        .doOnNext(order -> logger.info("Found order for order number: {}: {}", orderNumber, order))
                        .onErrorMap(error -> new GetOrderException(orderNumber, error));
    }


}
