package com.snap.reactive.demo.controllers;

import com.snap.reactive.demo.models.OrderSummary;
import com.snap.reactive.demo.services.GetOrderSummaryException;
import com.snap.reactive.demo.services.async.AsyncOrderSummaryService;
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

    private final AsyncOrderSummaryService asyncOrderSummaryService;

    @Autowired
    public AsyncOrderSummaryController(AsyncOrderSummaryService asyncOrderSummaryService) {
        this.asyncOrderSummaryService = asyncOrderSummaryService;
    }

    @GetMapping("/order/{number}")
    public CompletableFuture<OrderSummary> getOrderSummaryByNumber(@PathVariable("number") long number) {
        logger.info("Now obtaining order summary for order {}", number);
        long start = System.currentTimeMillis();
        return asyncOrderSummaryService.getOrderSummaryByNumber(number)
                                       .handle((summary, error) -> {
                                           if (error != null) {
                                               throw new GetOrderSummaryException(number, error);
                                           }
                                           long end = System.currentTimeMillis();
                                           logger.info("Obtained async response in {} ms: {}", (end - start), summary);
                                           return summary;
                                       });
    }


}


//    @GetMapping("/order/{number}")
//    public Order getOrderByNumber(@PathVariable("number") long orderNumber) {
//
//        long start = System.currentTimeMillis();
//        //blocking I/O
//        ResponseEntity<Order> response = restTemplate.getForEntity("http://localhost:4040/orders/12345", Order.class);
//        long end = System.currentTimeMillis();
//        System.out.printf("Obtained response in %d ms: %s%n", (end-start), response.getBody());
//
//        return response.getBody();
//    }

//    @GetMapping("/order/{number}")
//    public OrderSummary getOrderSummaryByNumber(@PathVariable("number") long number) {
//        long start = System.currentTimeMillis();
//        Order order = orderService.getOrderByNumber(number);
//        Customer customer = customerService.getCustomerByEmail(order.getCustomerEmail());
//        OrderSummary summary = new OrderSummary(customer, order);
//        long end = System.currentTimeMillis();
//        System.out.printf("Obtained response in %d ms: %s%n", (end-start), summary);
//        return summary;
//    }

//    @GetMapping("/order/{number}")
//    public CompletableFuture<OrderSummary> getOrderByNumber(@PathVariable("number") long number) {
//        AtomicLong start = new AtomicLong(System.currentTimeMillis());
//        return CompletableFuture.supplyAsync(() -> {
//            ResponseEntity<Order> response = restTemplate.getForEntity("http://localhost:4040/orders/12345", Order.class);
//            return response.getBody();
//        }, executorService)
//                                .thenApply(order -> {
//                                    ResponseEntity<Customer> response = restTemplate.getForEntity("http://localhost:4040/customer/{customerEmail}", Customer.class, order.getCustomerEmail());
//                                    return new OrderSummary(response.getBody(), order);
//                                })
//                                .handle((summary, error) -> {
//                                    if (error != null) {
//                                        throw new RuntimeException("Failed to obtain order summary", error);
//                                    }
//                                    long end = System.currentTimeMillis();
//                                    System.out.printf("Obtained response in %d ms: %s%n", (end - start.get()), summary);
//                                    return summary;
//                                });
//    }

//    @GetMapping("/order/{number}")
//    public CompletableFuture<OrderSummary> getOrderSummaryByNumber(@PathVariable("number") long number) {
//        long start = System.currentTimeMillis();
//        return asyncOrderService.getOrderByNumber(number)
//                                .thenCompose(order -> asyncCustomerService.getCustomerByEmail(order.getCustomerEmail())
//                                                                          .thenApply(customer -> new OrderSummary(customer, order)))
//                .handle((summary, error) -> {
//                    if(error != null) {
//                        throw new RuntimeException("Failed to obtain order summary", error);
//                    }
//                    long end = System.currentTimeMillis();
//                    System.out.printf("Obtained async response in %d ms: %s%n", (end - start), summary);
//                    return summary;
//                });
//    }
