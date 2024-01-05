package org.poc.soap.controller;

import lombok.RequiredArgsConstructor;
import org.poc.soap.client.CountryClient;
import org.poc.soap.controller.response.GetCountryRestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryClient countryClient;

    @GetMapping("/{countryName}")
    public GetCountryRestResponse getCountryName(@PathVariable String countryName) {
        var country = countryClient.getCountry(countryName);
        return GetCountryRestResponse.from(country);
    }

}
