package org.poc.soap.controller.response;

import org.poc.soap.wsdl.Country;
import org.poc.soap.wsdl.GetCountryResponse;

public record GetCountryRestResponse(
        String name,
        int population,
        String capital,
        String currency
) {

    public static GetCountryRestResponse from(Country country) {
        return new GetCountryRestResponse(
                country.getName(),
                country.getPopulation(),
                country.getCapital(),
                country.getCurrency().value()
        );
    }

}
