package org.poc.soap;

import lombok.extern.slf4j.Slf4j;
import org.poc.soap.exception.CountryNotFoundException;
import org.poc.soap.wsdl.Country;
import org.poc.soap.wsdl.GetCountryRequest;
import org.poc.soap.wsdl.GetCountryResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Slf4j
public class CountryClient extends WebServiceGatewaySupport {

    private final String countriesUrl;

    public CountryClient(String countriesBaseUrl, String countriesPath) {
        this.countriesUrl = countriesBaseUrl + countriesPath;
        log.info("Country client initialized with url: {}", countriesUrl);
    }

    public Country getCountry(String country) {
        GetCountryRequest request = new GetCountryRequest();
        request.setName(country);

        log.info("Requesting location for " + country);

        GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate()
                .marshalSendAndReceive(countriesUrl, request);

        if(response == null || response.getCountry() == null) {
            throw new CountryNotFoundException(country);
        }
        return response.getCountry();
    }

}
