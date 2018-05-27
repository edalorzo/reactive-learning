package com.snap.reactive.demo.api.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

@Data
public class Item {

    private final String product;
    private final int quantity;
    private final double price;

    @JsonCreator
    public Item(
            @JsonProperty("product") String product,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("price") double price) {

        requireNonNull(product, "The product must not be null");
        checkArgument(quantity > 0, "The quantity must be > 0");
        checkArgument(price >= 0, "The price must be >= 0");

        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
