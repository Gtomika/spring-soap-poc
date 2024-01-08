package org.poc.soap;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CountryIntegrationTest extends CountryIntegrationTestBase {

	private static final String COUNTRIES_REST_PATH = "/countries/{countryName}";

	@Test
	public void shouldReturnResponse_whenDownstreamSoapServiceReturnsCountry() throws Exception {
		stubSoapServiceToReturnCountry("Spain", 0);
		mockMvc.perform(MockMvcRequestBuilders.get(COUNTRIES_REST_PATH, "Spain"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Spain"))
				.andExpect(jsonPath("$.capital").value("Madrid"))
				.andExpect(jsonPath("$.currency").value("EUR"))
				.andExpect(jsonPath("$.population").value("46704314"));
	}

	@Test
	public void shouldReturnError_whenDownstreamSoapServiceReturnsCountryNotFound() throws Exception {
		stubSoapServiceToReturnCountryNotFound("Poland");
		mockMvc.perform(MockMvcRequestBuilders.get(COUNTRIES_REST_PATH, "Poland"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Country not found: Poland"));
	}



}
