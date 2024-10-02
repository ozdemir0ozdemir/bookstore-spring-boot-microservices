package com.ozdemir0ozdemir.bookstorewebapp.client;

import com.ozdemir0ozdemir.bookstorewebapp.ApplicationProperties;
import com.ozdemir0ozdemir.bookstorewebapp.client.catalog.CatalogServiceClient;
import com.ozdemir0ozdemir.bookstorewebapp.client.orders.OrderServiceClient;
import java.time.Duration;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientsConfig {

    private final ApplicationProperties properties;

    ClientsConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    RestClientCustomizer restClientCustomizer() {
        return restClientBuilder -> restClientBuilder
                .baseUrl(properties.apiGatewayUrl())
                .requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS
                        .withConnectTimeout(Duration.ofSeconds(5))
                        .withReadTimeout(Duration.ofSeconds(5))));
    }

    @Bean
    CatalogServiceClient catalogServiceClient() {
        RestClient restClient = RestClient.create(properties.apiGatewayUrl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(CatalogServiceClient.class);
    }

    @Bean
    OrderServiceClient orderServiceClient() {
        RestClient restClient = RestClient.create(properties.apiGatewayUrl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(OrderServiceClient.class);
    }
}
