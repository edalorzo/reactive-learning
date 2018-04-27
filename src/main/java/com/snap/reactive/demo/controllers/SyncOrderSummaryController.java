package com.snap.reactive.demo.controllers;

import com.snap.reactive.demo.models.OrderSummary;
import com.snap.reactive.demo.services.sync.OrderSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
class SyncOrderSummaryController {

    private final OrderSummaryService orderSummaryService;

    @Autowired
    public SyncOrderSummaryController(OrderSummaryService orderSummaryService) {
        this.orderSummaryService = orderSummaryService;
    }

    @GetMapping("/order/{number}")
    public OrderSummary getOrderSummaryByNumber(@PathVariable("number") long number) {
        long start = System.currentTimeMillis();
        OrderSummary summary = orderSummaryService.getOrderSummaryByNumber(number);
        long end = System.currentTimeMillis();
        System.out.printf("Obtained response in %d ms: %s%n", (end - start), summary);
        return summary;
    }

}
