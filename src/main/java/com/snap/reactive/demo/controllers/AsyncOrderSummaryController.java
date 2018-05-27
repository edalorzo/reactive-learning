package com.snap.reactive.demo.controllers;

import com.snap.reactive.demo.api.async.OrderSummaryService;
import com.snap.reactive.demo.api.models.OrderSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/async")
class AsyncOrderSummaryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderSummaryService orderSummaryService;

    @Autowired
    public AsyncOrderSummaryController(OrderSummaryService orderSummaryService) {
        this.orderSummaryService = orderSummaryService;
    }

    @GetMapping("/order/{number}")
    public CompletableFuture<OrderSummary> getOrderSummaryByNumber(@PathVariable("number") long number) {
        logger.info("Now getting order summary for order {}", number);
        long start = System.currentTimeMillis();

        CompletableFuture<OrderSummary> promise = orderSummaryService.getOrderSummaryByNumber(number)
                                                                     .thenApply(summary -> {
                                                                         long end = System.currentTimeMillis();
                                                                         logger.info("Obtained response in {} ms: {}", (end - start), summary);
                                                                         return summary;
                                                                     });
        logger.info("Good-bye");
        return promise;


    }
}