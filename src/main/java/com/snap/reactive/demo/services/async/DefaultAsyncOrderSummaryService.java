package com.snap.reactive.demo.services.async;

import com.snap.reactive.demo.models.Customer;
import com.snap.reactive.demo.models.Order;
import com.snap.reactive.demo.models.OrderSummary;
import com.snap.reactive.demo.services.GetCustomerException;
import com.snap.reactive.demo.services.GetOrderSummaryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
class DefaultAsyncOrderSummaryService implements AsyncOrderSummaryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncOrderService orderService;
    private final AsyncCustomerService customerService;

    @Autowired
    public DefaultAsyncOrderSummaryService(AsyncOrderService orderService, AsyncCustomerService customerService) {
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
            logger.info("Build summary for order number {}: {}", orderNumber, summary);
            return summary;
        };
    }

    private Function<Order, CompletableFuture<OrderSummary>> getOrderSummary() {
        return order -> customerService.getCustomerByEmail(order.getCustomerEmail())
                                       .thenApply(customer -> new OrderSummary(customer, order));
    }
}
