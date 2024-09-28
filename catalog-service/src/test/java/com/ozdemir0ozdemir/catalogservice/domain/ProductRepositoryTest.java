package com.ozdemir0ozdemir.catalogservice.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {"spring.test.database.replace:=none", "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db"})
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldGetAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        assertEquals(15, products.size());
    }

    @Test
    void shouldGetProductByCode() {
        ProductEntity product = productRepository.findByCode("P100").orElseThrow();
        assertEquals("P100", product.getCode());
        assertEquals("Steve Jobs", product.getName());
        assertEquals("Biografi", product.getDescription());
        assertEquals(new BigDecimal("20"), product.getPrice());
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistingProduct() {
        Optional<ProductEntity> product = productRepository.findByCode("P1000");
        assertTrue(product.isEmpty());
    }
}
