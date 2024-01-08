package org.poc.soap;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StopWatch;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class CountryAggregationIntegrationTest extends CountryIntegrationTestBase {

    private static final String COUNTRIES_AGGREGATION_REST_PATH = "/countries/aggregate/{countryNames}";

    @Test
    public void shouldReturnResponse_whenDownstreamSoapServiceReturnsCountries() throws Exception {
        //both request are delayed by 1 second, to prove that the aggregation is done in parallel
        stubSoapServiceToReturnCountry("Spain", 1000);
        stubSoapServiceToReturnCountry("Poland", 1000);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        mockMvc.perform(MockMvcRequestBuilders.get(COUNTRIES_AGGREGATION_REST_PATH, "Spain,Poland"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Spain"))
                .andExpect(jsonPath("$[0].capital").value("Madrid"))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[0].population").value("46704314"))
                .andExpect(jsonPath("$[1].name").value("Poland"))
                .andExpect(jsonPath("$[1].capital").value("Warsaw"))
                .andExpect(jsonPath("$[1].currency").value("PLN"))
                .andExpect(jsonPath("$[1].population").value("38186860"));

        stopWatch.stop();
        log.info("Test took {} ms", stopWatch.getTotalTimeMillis());
        assertTrue(stopWatch.getTotalTimeMillis() < 2000);
        //also: check application logs to see service timings
    }

    @Test
    @Disabled("Due to some WireMock issue, this test fails")
    public void shouldReturnNotFound_whenDownstreamSoapService_returnsNotFoundForOneCountry() throws Exception {
        stubSoapServiceToReturnCountry("Spain", 0);
        stubSoapServiceToReturnCountryNotFound("Poland");

        mockMvc.perform(MockMvcRequestBuilders.get(COUNTRIES_AGGREGATION_REST_PATH, "Spain,Poland"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Country not found: Poland"));;
    }

}
