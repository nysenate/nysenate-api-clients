package gov.nysenate;

public record Office(String name, String street, String city, String postalCode,
                     String phone, String fax, String additional,
                     String province, String provinceName, String country, String countryName) {}
