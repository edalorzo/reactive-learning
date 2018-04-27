package com.snap.reactive.demo.controllers;

import com.snap.reactive.demo.models.OrderSummary;
import com.snap.reactive.demo.services.rx.RxOrderSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rx")
class RxOrderSummaryController {

    private final RxOrderSummaryService orderSummaryService;

    @Autowired
    public RxOrderSummaryController(RxOrderSummaryService orderSummaryService) {
        this.orderSummaryService = orderSummaryService;
    }

    @GetMapping("/order/{number}")
    public Mono<OrderSummary> getOrderSummaryByNumber(@PathVariable("number") long number) {
        return orderSummaryService.getOrderSummaryByNumber(number);
    }

}
