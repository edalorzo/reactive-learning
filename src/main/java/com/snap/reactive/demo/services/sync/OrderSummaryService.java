package com.snap.reactive.demo.services.sync;

import com.snap.reactive.demo.models.OrderSummary;

public interface OrderSummaryService {
    OrderSummary getOrderSummaryByNumber(long orderNumber);
}
