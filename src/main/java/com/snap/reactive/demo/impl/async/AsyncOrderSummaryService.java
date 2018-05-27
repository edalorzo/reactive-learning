package com.snap.reactive.demo.impl.async;

import com.snap.reactive.demo.api.GetOrderSummaryException;
import com.snap.reactive.demo.api.async.CustomerService;
import com.snap.reactive.demo.api.async.OrderService;
import com.snap.reactive.demo.api.async.OrderSummaryService;
import com.snap.reactive.demo.api.models.Order;
import com.snap.reactive.demo.api.models.OrderSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
class AsyncOrderSummaryService implements OrderSummaryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;
    private final CustomerService customerService;

    @Autowired
    public AsyncOrderSummaryService(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public CompletableFuture<OrderSummary> getOrderSummaryByNumber(long orderNumber) {
        return orderService.getOrderByNumber(orderNumber)
                           .thenCompose(getOrderSummary())
                           .handle(getOrderSummaryOrElseFail(orderNumber));
    }

    private BiFunction<OrderSummary, Throwable, OrderSummary> getOrderSummaryOrElseFail(long orderNumber) {
        return (summary, error) -> {
            if (error != null) {
                throw new GetOrderSummaryException(orderNumber, error);
            }
            logger.info("Built order summary for order number {}: {}", orderNumber, summary);
            return summary;
        };
    }

    private Function<Order, CompletableFuture<OrderSummary>> getOrderSummary() {
        return order -> customerService.getCustomerByEmail(order.getCustomerEmail())
                                       .thenApply(customer -> new OrderSummary(customer, order));
    }
}
