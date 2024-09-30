package com.ozdemir0ozdemir.orderservice.web.controllers;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ozdemir0ozdemir.orderservice.AbstractIT;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests {

        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("P101", "Steve Jobs", BigDecimal.valueOf(20.0));
            String payload =
                    """
                        {
                           "customer": {
                             "name": "Özdemir Özdemir",
                             "email": "ozdemir@hotmail.com",
                             "phone": "123123123"
                           },
                           "deliveryAddress": {
                             "line1": "İstanbul",
                             "city": "İstanbul",
                             "state": "İstanbul",
                             "zip": "34000",
                             "country": "Türkiye"
                           },
                           "items": [
                             {
                               "code": "P101",
                               "name": "Steve Jobs",
                               "price": 20,
                               "quantity": 1
                             }
                           ]
                         }
                    """;

            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("api/v1/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", Matchers.notNullValue());
        }

        @Test
        void shouldReturnBadRequestMandatoryDataMissing() {
            String payload =
                    """
                        {
                          "items": [
                            {
                              "code": "P101",
                              "name": "Product",
                              "price": 10.23,
                              "quantity": 0
                            }
                          ],
                          "customer": {
                            "name": "",
                            "email": "ozdemirozdemir@hotmail.com.tr",
                            "phone": "23123123123"
                          },
                          "deliveryAddress": {
                            "line1": "istanbul",
                            "line2": "istanbul",
                            "city": "istanbul",
                            "state": "istanbul",
                            "zip": "34000",
                            "country": ""
                          }
                        }
                        """;

            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("api/v1/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
