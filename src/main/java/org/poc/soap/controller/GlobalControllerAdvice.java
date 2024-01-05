package org.poc.soap.controller;

import org.poc.soap.controller.response.ErrorResponse;
import org.poc.soap.exception.CountryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CountryNotFoundException.class)
    public ErrorResponse handleCountryNotFoundException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

}
