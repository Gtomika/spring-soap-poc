package org.poc.soap;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0) //random port
@SpringBootTest(properties = "soap.countries.base-url=http://localhost:${wiremock.server.port}")
class CountryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Value("${soap.countries.path}")
	private String countriesPath;

	@Test
	public void shouldReturnResponse_whenDownstreamSoapServiceReturnsCountry() throws Exception {
		stubSoapServiceToReturnCountry();
		mockMvc.perform(MockMvcRequestBuilders.get("/countries/Spain"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Spain"))
				.andExpect(jsonPath("$.capital").value("Madrid"))
				.andExpect(jsonPath("$.currency").value("EUR"))
				.andExpect(jsonPath("$.population").value("46704314"));
	}

	@Test
	public void shouldReturnError_whenDownstreamSoapServiceReturnsCountryNotFound() throws Exception {
		stubSoapServiceToReturnCountryNotFound();
		mockMvc.perform(MockMvcRequestBuilders.get("/countries/Uganda"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("Country not found: Uganda"));
	}

	private void stubSoapServiceToReturnCountry() {
		stubFor(WireMock.post(urlEqualTo(countriesPath))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/xml;charset=UTF-8")
						.withBodyFile("country-response.xml")));
	}

	private void stubSoapServiceToReturnCountryNotFound() {
		stubFor(WireMock.post(urlEqualTo(countriesPath))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/xml;charset=UTF-8")
						.withBodyFile("country-not-found-response.xml")));
	}

}
