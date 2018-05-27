package com.snap.reactive.demo.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {

    private final int status;
    private final String message;

    @JsonCreator
    public Error(@JsonProperty("status") int status,
                 @JsonProperty("message") String message) {

        this.status = status;
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
