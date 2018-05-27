package com.snap.reactive.demo.api.sync;

import com.snap.reactive.demo.api.models.OrderSummary;

public interface OrderSummaryService {
    OrderSummary getOrderSummaryByNumber(long orderNumber);
}
