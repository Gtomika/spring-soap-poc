package org.poc.soap;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToXml;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@Slf4j
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0) //random port
@SpringBootTest(properties = "soap.countries.base-url=http://localhost:${wiremock.server.port}")
public class CountryIntegrationTestBase {

    private static final String COUNTRY_REQUEST_FILE_TEMPLATE = "classpath:__files/country-%s-request.xml";
    //this one is already relative to classpath:__files
    private static final String COUNTRY_RESPONSE_FILE_TEMPLATE = "country-%s-response.xml";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ResourceLoader resourceLoader;

    @Value("${soap.countries.path}")
    protected String countriesSoapPath;

    protected void stubSoapServiceToReturnCountry(String countryName, int delayMs) throws IOException {
        countryName = countryName.toLowerCase();
        String requestFileLocation = COUNTRY_REQUEST_FILE_TEMPLATE.formatted(countryName);
        String responseFileLocation = COUNTRY_RESPONSE_FILE_TEMPLATE.formatted(countryName);
        log.info("""
                Mocking SOAP request for country '{}'.
                - Using request file: {}
                - Using response file: {}""", countryName, requestFileLocation, responseFileLocation);

        String spainRequestContent = resourceLoader.getResource(requestFileLocation)
                .getContentAsString(Charset.defaultCharset());
        stubFor(WireMock.post(urlEqualTo(countriesSoapPath))
                .withRequestBody(equalToXml(spainRequestContent))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml;charset=UTF-8")
                        .withBodyFile(responseFileLocation)
                        .withFixedDelay(delayMs)));
    }

    protected void stubSoapServiceToReturnCountryNotFound(String countryName) throws IOException {
        countryName = countryName.toLowerCase();
        String requestFileLocation = COUNTRY_REQUEST_FILE_TEMPLATE.formatted(countryName);
        log.info("Request for country '{}' will return country not found SOAP response. Request file: {}",
                countryName, requestFileLocation);

        String notFoundCountryRequestContent = resourceLoader.getResource(requestFileLocation)
                .getContentAsString(Charset.defaultCharset());
        stubFor(WireMock.post(urlEqualTo(countriesSoapPath))
                .withRequestBody(equalToXml(notFoundCountryRequestContent))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml;charset=UTF-8")
                        .withBodyFile("country-not-found-response.xml")));
    }

}
