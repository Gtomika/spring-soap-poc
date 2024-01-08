package org.poc.soap;

public record CountryModel(
        String name,
        int population,
        String capital,
        String currency
) {

    public static CountryModel fromSoap(org.poc.soap.wsdl.Country country) {
        return new CountryModel(
                country.getName(),
                country.getPopulation(),
                country.getCapital(),
                country.getCurrency().value()
        );
    }

}
