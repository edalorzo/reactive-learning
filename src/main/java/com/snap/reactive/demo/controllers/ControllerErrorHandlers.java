package com.snap.reactive.demo.controllers;

import com.snap.reactive.demo.models.Error;
import com.snap.reactive.demo.services.GetOrderSummaryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ControllerErrorHandlers {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //private final BinaryExceptionClassifier clientErrorClassifier;

    public ControllerErrorHandlers() {
        //clientErrorClassifier = new BinaryExceptionClassifier()
    }

    @ExceptionHandler(GetOrderSummaryException.class)
    public ResponseEntity<Error> handle(GetOrderSummaryException ex) {

        logger.error(ex.getMessage(), ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
                             .body(new Error(status.value(), ex.getMessage()));
    }


}
