# Spring SOAP PoC

This PoC application exposes a REST API that calls out to a SOAP 
service to retrieve the country information.

The reactor framework is used to make the SOAP call non-blocking 
and parallel. This is similar to how the `WebClient` works, for REST.

## How to run

Download and run the complete SOAP service from [here](https://github.com/spring-guides/gs-producing-web-service/tree/main/complete).
This is an example SOAP producing app from the Spring guides. This PoC
application is configured to call out to this service running locally.
The SOAP service supports only following countries: Spain, Poland.

Then run the SOAP application with `mvn spring-boot:run` from its directory.

Finally, run this PoC application with `mvn spring-boot:run` from this directory.

You can make calls to `http://localhost:8085/countries/{country}` or `http://localhost:8085/countries/aggregate/{countries}`
to test the application manually.

## Integration tests

Some integration tests are provided in the `src/test/java` folder. 
These tests are using the [WireMock](http://wiremock.org/) library to mock the SOAP service.