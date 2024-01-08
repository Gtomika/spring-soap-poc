package org.poc.soap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryClient countryClient;

    public CountryModel getCountryByName(String countryName) {
        var country = countryClient.getCountry(countryName);
        return CountryModel.fromSoap(country);
    }

    public List<CountryModel> getCountriesByNames(List<String> countryNames) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        var countryMonos = countryNames.stream()
                .map(this::createCountryRequestMono)
                .toList();
        var countries = Mono.zip(countryMonos, this::combineCountryRequestMonos)
                .block();

        stopWatch.stop();
        log.info("Got countries in {} ms", stopWatch.getTotalTimeMillis());
        return countries;
    }

    private Mono<CountryModel> createCountryRequestMono(String countryName) {
        return Mono.fromCallable(() -> getCountryByName(countryName))
                .subscribeOn(Schedulers.boundedElastic()); //this is the key to parallelize requests
    }

    private List<CountryModel> combineCountryRequestMonos(Object[] objects) {
        return Arrays.stream(objects)
                .map(CountryModel.class::cast)
                .toList();
    }
}
