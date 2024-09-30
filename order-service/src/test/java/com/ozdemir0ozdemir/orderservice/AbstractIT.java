package com.ozdemir0ozdemir.orderservice;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public abstract class AbstractIT {

    @LocalServerPort
    int port;

    static WireMockContainer wireMockContainer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine");

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeAll
    static void beforeAll() {
        wireMockContainer.start();
        WireMock.configureFor(wireMockContainer.getHost(), wireMockContainer.getPort());
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("orders.catalog-service-url", wireMockContainer::getBaseUrl);
    }

    protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/products/" + code))
                .willReturn(AbstractIT.createResponseForWireMock(code, name, price)));
    }

    private static ResponseDefinitionBuilder createResponseForWireMock(String code, String name, BigDecimal price) {
        return WireMock.aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withStatus(200)
                .withBody(
                        """
                        {
                          "code": "%s",
                          "name": "%s",
                          "price": %f
                        }
                        """
                                .formatted(code, name, price.doubleValue()));
    }
}
