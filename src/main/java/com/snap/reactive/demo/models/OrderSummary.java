package com.snap.reactive.demo.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static java.util.Objects.requireNonNull;

@Data
public class OrderSummary {
    private final Customer customer;
    private final Order order;

    @JsonCreator
    public OrderSummary(@JsonProperty("customer") Customer customer,
                        @JsonProperty("order") Order order) {

        requireNonNull(customer, "The customer must not be null");
        requireNonNull(order, "The order must not be null");

        this.customer = customer;
        this.order = order;
    }
}
