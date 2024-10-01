package com.ozdemir0ozdemir.orderservice.client.catalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class ProductServiceClient {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestClient catalogServiceRestClient;

    public ProductServiceClient(RestClient catalogServiceRestClient) {
        this.catalogServiceRestClient = catalogServiceRestClient;
    }

    @CircuitBreaker(name = "catalog-service-get-product-by-code")
    @Retry(name = "catalog-service-get-product-by-code", fallbackMethod = "getProductByCodeFallback")
    public Optional<Product> getProductByCode(String code) {
        log.info("Fetching product from code: {}", code);

        Product product = catalogServiceRestClient
                .get()
                .uri("/api/v1/products/{code}", code)
                .retrieve()
                .body(Product.class);

        return Optional.ofNullable(product);
    }

    public Optional<Product> getProductByCodeFallback(String code, Throwable t) {
        log.error(
                "ProductServiceClient.getProductByCodeFallback: code: \"{}\" with message: \"{}\"",
                code,
                t.getMessage());
        return Optional.empty();
    }

    public List<Product> getAllProducts() {
        log.info("Fetching all products");

        Product[] products = catalogServiceRestClient
                .get()
                .uri(ProductServiceClient.getAllProductsUri(1, 5))
                .retrieve()
                .body(Product[].class);

        if (products == null) {
            return List.of();
        }
        return Arrays.stream(products).toList();
    }

    private static URI getAllProductsUri(int page, int pageSize) {
        return new DefaultUriBuilderFactory()
                .builder()
                .path("api/v1/products")
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .build();
    }
}
