package org.poc.soap.exception;

import lombok.Getter;

@Getter
public class CountryNotFoundException extends RuntimeException {

    private final String country;

    public CountryNotFoundException(String country) {
        super("Country not found: " + country);
        this.country = country;
    }

}
