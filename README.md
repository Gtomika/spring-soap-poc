# Spring SOAP PoC

This PoC application exposes a REST API `/countries/{countryName}`, 
that calls out to a SOAP service to retrieve the country information.

## How to run

Download and run the complete SOAP service from [here](https://github.com/spring-guides/gs-producing-web-service/tree/main/complete).

Then run the application with `mvn spring-boot:run`.

## How to test

Some integration tests are provided in the `src/test/java` folder. 
These tests are using the [WireMock](http://wiremock.org/) library to mock the SOAP service.