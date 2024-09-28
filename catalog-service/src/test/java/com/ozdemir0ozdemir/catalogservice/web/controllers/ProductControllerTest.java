package com.ozdemir0ozdemir.catalogservice.web.controllers;

import static io.restassured.RestAssured.given;

import com.ozdemir0ozdemir.catalogservice.AbstractIntegrationTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class ProductControllerTest extends AbstractIntegrationTest {

    @Test
    void shouldResultProducts() {
        given().contentType(ContentType.JSON)
                .when()
                .get("api/v1/products")
                .then()
                .statusCode(200)
                .body("data", Matchers.hasSize(10))
                .body("totalElements", Matchers.is(15))
                .body("pageNumber", Matchers.is(1))
                .body("totalPages", Matchers.is(2))
                .body("isFirst", Matchers.is(true))
                .body("isLast", Matchers.is(false))
                .body("hasNext", Matchers.is(true))
                .body("hasPrevious", Matchers.is(false));
    }

    @Test
    void shouldReturnProductByCode() {
        given().contentType(ContentType.JSON)
                .when()
                .get("api/v1/products/{code}", "P100")
                .then()
                .statusCode(200)
                .body("code", Matchers.is("P100"))
                .body("name", Matchers.is("Steve Jobs"))
                .body("description", Matchers.is("Biografi"))
                .body(
                        "imageUrl",
                        Matchers.is("https://m.media-amazon.com/images/I/71sVQDj0SCL._AC_UF1000,1000_QL80_.jpg"))
                .body("price", Matchers.is(20));
    }

    @Test
    void shouldReturnNotFoundWhenProductCodeNotExists() {
        String code = "invalid_product_code";

        // FIXME: get() throws an exception and RestAssured cannot catch it then test fails instead of it should pass
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/products/{code}", code)
                .then()
                .statusCode(404)
                .body("status", Matchers.is(404))
                .body("title", Matchers.is("Product Not Found"))
                .body("detail", Matchers.is("Product not found with code: " + code));
    }
}
