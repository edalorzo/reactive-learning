package com.snap.reactive.demo.controllers;

import com.snap.reactive.demo.api.models.OrderSummary;
import com.snap.reactive.demo.api.rx.OrderSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rx")
class RxOrderSummaryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OrderSummaryService orderSummaryService;

    @Autowired
    public RxOrderSummaryController(OrderSummaryService orderSummaryService) {
        this.orderSummaryService = orderSummaryService;
    }

    @GetMapping("/order/{number}")
    public Mono<OrderSummary> getOrderSummaryByNumber(@PathVariable("number") long number) {
        logger.info("Now getting order summary for order {}", number);
        long start = System.currentTimeMillis();
        Mono<OrderSummary> promise = orderSummaryService.getOrderSummaryByNumber(number)
                                                        .doOnNext(summary -> {
                                                            long end = System.currentTimeMillis();
                                                            logger.info("Obtained response in {} ms: {}", (end - start), summary);
                                                        });
        logger.info("Good-bye");
        return promise;
    }

}
