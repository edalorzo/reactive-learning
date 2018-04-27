package com.snap.reactive.demo.services.sync;

import com.snap.reactive.demo.models.Order;

public interface OrderService {
    Order getOrderByNumber(long orderNumber);
}
