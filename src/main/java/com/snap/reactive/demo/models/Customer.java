package com.snap.reactive.demo.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static java.util.Objects.requireNonNull;

@Data
public class Customer {

    private final String email;
    private final String firstName;
    private final String lastName;


    @JsonCreator
    public Customer(
            @JsonProperty("email") String email,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName) {

        requireNonNull(email, "The email must not be null");
        requireNonNull(firstName, "The firstName must not be null");
        requireNonNull(lastName, "The lastName must not be null");

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
