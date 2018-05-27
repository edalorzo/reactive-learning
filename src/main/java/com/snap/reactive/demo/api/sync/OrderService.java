package com.snap.reactive.demo.api.sync;

import com.snap.reactive.demo.api.models.Order;

public interface OrderService {
    Order getOrderByNumber(long orderNumber);
}
