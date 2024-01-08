package org.poc.soap.controller.response;

import org.poc.soap.CountryModel;

public record GetCountryRestResponse(
        String name,
        int population,
        String capital,
        String currency
) {

    public static GetCountryRestResponse from(CountryModel country) {
        return new GetCountryRestResponse(
                country.name(),
                country.population(),
                country.capital(),
                country.currency()
        );
    }

}
