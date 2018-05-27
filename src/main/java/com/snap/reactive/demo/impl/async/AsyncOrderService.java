package com.snap.reactive.demo.impl.async;

import com.snap.reactive.demo.api.GetOrderException;
import com.snap.reactive.demo.api.async.OrderService;
import com.snap.reactive.demo.api.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@Service
class AsyncOrderService implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncRestTemplate restTemplate;

    @Autowired
    AsyncOrderService(AsyncRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CompletableFuture<Order> getOrderByNumber(long orderNumber) {
        return performRequest(orderNumber).handle(getOrderOrElseFail(orderNumber));

    }

    private BiFunction<Order, Throwable, Order> getOrderOrElseFail(long orderNumber) {
        return (order, error) -> {
            if(error != null) {
                throw new GetOrderException(orderNumber, error);
            }
            logger.info("Found order for order number: {}: {}", orderNumber, order);
            return order;
        };
    }

    private CompletableFuture<Order> performRequest(long orderNumber) {
        String endpoint = "http://localhost:4040/orders/{orderNumber}";
        ListenableFuture<ResponseEntity<Order>> future = restTemplate.getForEntity(endpoint, Order.class, orderNumber);
        return future.completable().thenApply(ResponseEntity::getBody);
    }
}
