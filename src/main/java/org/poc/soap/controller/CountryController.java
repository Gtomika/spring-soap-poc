package org.poc.soap.controller;

import lombok.RequiredArgsConstructor;
import org.poc.soap.CountryService;
import org.poc.soap.controller.response.GetCountryRestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/{countryName}")
    public GetCountryRestResponse getCountryByName(@PathVariable String countryName) {
        var country = countryService.getCountryByName(countryName);
        return GetCountryRestResponse.from(country);
    }

    @GetMapping("aggregate/{countryNames}")
    public List<GetCountryRestResponse> getCountriesByNames(@PathVariable List<String> countryNames) {
        var countries = countryService.getCountriesByNames(countryNames);
        return countries.stream()
                .map(GetCountryRestResponse::from)
                .toList();
    }

}
