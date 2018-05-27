package com.snap.reactive.demo.controllers;

import com.snap.reactive.demo.api.models.OrderSummary;
import com.snap.reactive.demo.api.sync.OrderSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
class SyncOrderSummaryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
        logger.info("Obtained response in {} ms: {}", (end - start), summary);
        return summary;
    }

}
