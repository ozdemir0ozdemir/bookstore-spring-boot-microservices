package com.ozdemir0ozdemir.orderservice.domain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OrderItem(
        @NotBlank(message = "Code is requuired") String code,
        @NotBlank(message = "Name is requuired") String name,
        @NotNull(message = "Price is requuired") BigDecimal price,
        @NotNull @Min(1) Integer quantity) {}
