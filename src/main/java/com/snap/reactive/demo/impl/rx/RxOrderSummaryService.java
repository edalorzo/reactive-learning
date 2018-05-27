package com.snap.reactive.demo.impl.rx;

import com.snap.reactive.demo.api.GetOrderSummaryException;
import com.snap.reactive.demo.api.models.Order;
import com.snap.reactive.demo.api.models.OrderSummary;
import com.snap.reactive.demo.api.rx.CustomerService;
import com.snap.reactive.demo.api.rx.OrderService;
import com.snap.reactive.demo.api.rx.OrderSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
class RxOrderSummaryService implements OrderSummaryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;
    private final CustomerService customerService;

    @Autowired
    public RxOrderSummaryService(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public Mono<OrderSummary> getOrderSummaryByNumber(long orderNumber) {
        return orderService.getOrderByNumber(orderNumber)
                           .flatMap(getOrderSummary())
                           .doOnNext(summary -> logger.info("Build summary for order number {}: {}", orderNumber, summary))
                           .onErrorMap(error -> new GetOrderSummaryException(orderNumber, error));
    }

    private Function<Order, Mono<OrderSummary>> getOrderSummary() {
        return order -> customerService.getCustomerByEmail(order.getCustomerEmail())
                                       .map(customer -> new OrderSummary(customer, order));
    }
}
