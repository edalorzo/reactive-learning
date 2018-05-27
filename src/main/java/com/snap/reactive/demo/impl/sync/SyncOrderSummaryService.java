package com.snap.reactive.demo.impl.sync;

import com.snap.reactive.demo.api.GetOrderSummaryException;
import com.snap.reactive.demo.api.models.Customer;
import com.snap.reactive.demo.api.models.Order;
import com.snap.reactive.demo.api.models.OrderSummary;
import com.snap.reactive.demo.api.sync.CustomerService;
import com.snap.reactive.demo.api.sync.OrderService;
import com.snap.reactive.demo.api.sync.OrderSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SyncOrderSummaryService implements OrderSummaryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;
    private final CustomerService customerService;

    @Autowired
    public SyncOrderSummaryService(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public OrderSummary getOrderSummaryByNumber(long orderNumber) {
        logger.info("Now getting order summary for {}", orderNumber);
        try {
            Order order = orderService.getOrderByNumber(orderNumber);
            Customer customer = customerService.getCustomerByEmail(order.getCustomerEmail());
            return new OrderSummary(customer, order);
        } catch (Exception cause) {
            throw new GetOrderSummaryException(orderNumber, cause);
        }
    }
}
