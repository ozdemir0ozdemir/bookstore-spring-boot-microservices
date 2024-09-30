package com.ozdemir0ozdemir.orderservice.client.catalog;

import com.ozdemir0ozdemir.orderservice.OrderServiceProperties;
import java.time.Duration;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class CatalogServiceClientConfig {

    @Bean
    RestClient catalogServiceRestClient(OrderServiceProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.catalogServiceUrl())
                .requestFactory(CatalogServiceClientConfig.getClientHttpRequestFactory())
                .build();
    }

    private static ClientHttpRequestFactory getClientHttpRequestFactory() {
        return ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(5))
                .withReadTimeout(Duration.ofSeconds(5)));
    }
}
