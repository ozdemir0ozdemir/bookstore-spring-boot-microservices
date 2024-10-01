package com.ozdemir0ozdemir.orderservice.web.controllers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import com.ozdemir0ozdemir.orderservice.AbstractIT;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderSummary;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
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

    @Nested
    class GetOrdersTests {

        @Test
        void shouldGetOrderSuccessfully() {
            List<OrderSummary> orderSummaryList = given().when()
                    .get("/api/v1/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertEquals(2, orderSummaryList.size());
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("/api/v1/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is(orderNumber))
                    .body("items.size()", is(2));
        }
    }
}
