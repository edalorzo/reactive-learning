package com.snap.reactive.demo.services.rx;

import com.snap.reactive.demo.models.Order;
import com.snap.reactive.demo.models.OrderSummary;
import com.snap.reactive.demo.services.GetOrderSummaryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
class DefaultRxOrderSummaryService implements RxOrderSummaryService {

    private final RxOrderService orderService;
    private final RxCustomerService customerService;

    @Autowired
    public DefaultRxOrderSummaryService(RxOrderService orderService, RxCustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public Mono<OrderSummary> getOrderSummaryByNumber(long orderNumber) {
        return orderService.getOrderByNumber(orderNumber)
                           .flatMap(getOrderSummary())
                           .onErrorMap(error -> new GetOrderSummaryException(orderNumber, error));
    }

    private Function<Order, Mono<OrderSummary>> getOrderSummary() {
        return order -> customerService.getCustomerByEmail(order.getCustomerEmail())
                                       .map(customer -> new OrderSummary(customer, order));
    }
}
