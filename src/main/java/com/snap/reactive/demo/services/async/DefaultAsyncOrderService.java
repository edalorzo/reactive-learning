package com.snap.reactive.demo.services.async;

import com.snap.reactive.demo.models.Order;
import com.snap.reactive.demo.services.GetOrderException;
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
class DefaultAsyncOrderService implements AsyncOrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncRestTemplate restTemplate;
    //private final ExecutorService executorService;

    @Autowired
    DefaultAsyncOrderService(AsyncRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        //this.executorService = executorService;
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
