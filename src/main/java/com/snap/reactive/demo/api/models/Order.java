package com.snap.reactive.demo.api.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

import static java.util.Objects.requireNonNull;


@Data
public class Order {

    private final long number;
    private final Collection<Item> items;
    private final String customerEmail;

    @Builder
    @JsonCreator
    public Order(
            @JsonProperty("number") long number,
            @JsonProperty("items") Collection<Item> items,
            @JsonProperty("customerEmail") String customerEmail) {

        requireNonNull(items, "The items must not be null");
        requireNonNull(customerEmail, "The customer must not be null");

        this.number = number;
        this.items = items;
        this.customerEmail = customerEmail;
    }

}
