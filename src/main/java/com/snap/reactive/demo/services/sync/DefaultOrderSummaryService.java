package com.snap.reactive.demo.services.sync;

import com.snap.reactive.demo.models.Customer;
import com.snap.reactive.demo.models.Order;
import com.snap.reactive.demo.models.OrderSummary;
import com.snap.reactive.demo.services.GetOrderSummaryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class DefaultOrderSummaryService implements OrderSummaryService {

    private final OrderService orderService;
    private final CustomerService customerService;

    @Autowired
    public DefaultOrderSummaryService(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @Override
    public OrderSummary getOrderSummaryByNumber(long orderNumber) {
        try {
            Order order = orderService.getOrderByNumber(orderNumber);
            Customer customer = customerService.getCustomerByEmail(order.getCustomerEmail());
            return new OrderSummary(customer, order);
        } catch (Exception cause) {
            throw new GetOrderSummaryException(orderNumber, cause);
        }
    }
}
