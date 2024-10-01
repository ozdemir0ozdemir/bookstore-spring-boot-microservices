package com.ozdemir0ozdemir.notificationservice.domain.models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public record Address(
        @NotBlank(message = "Delivery address is required") String line1,
        String line2,
        @NotBlank(message = "Delivery city is required") String city,
        @NotBlank(message = "Delivery state is required") String state,
        @NotBlank(message = "Delivery ZipCode required") String zip,
        @NotBlank(message = "Delivery country is required") String country) {}
