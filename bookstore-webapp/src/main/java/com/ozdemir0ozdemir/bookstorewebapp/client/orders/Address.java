package com.ozdemir0ozdemir.bookstorewebapp.client.orders;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record Address(
        @NotBlank(message = "AddressLine1 is required") String line1,
        String line2,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "State is required") String state,
        @NotBlank(message = "Zip is required") String zip,
        @NotBlank(message = "Country is required") String country)
        implements Serializable {}
