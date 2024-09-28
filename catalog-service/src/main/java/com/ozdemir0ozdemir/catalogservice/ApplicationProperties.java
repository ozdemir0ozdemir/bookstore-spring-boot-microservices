package com.ozdemir0ozdemir.catalogservice;

import jakarta.validation.constraints.Min;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "catalog")
public record ApplicationProperties(
        @DefaultValue("10")
        @Min(1)
        int pageSize
) {
}
