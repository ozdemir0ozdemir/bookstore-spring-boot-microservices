package com.ozdemir0ozdemir.orderservice.domain;

import com.ozdemir0ozdemir.orderservice.client.catalog.Product;
import com.ozdemir0ozdemir.orderservice.client.catalog.ProductServiceClient;
import com.ozdemir0ozdemir.orderservice.domain.models.CreateOrderRequest;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderItem;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {
    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient productServiceClient;

    public OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    public void validate(CreateOrderRequest request) {
        Set<OrderItem> items = request.items();
        for (OrderItem item : items) {
            Product product = productServiceClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product code: " + item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                log.error(
                        "Product price not matching. Actual price: {}, received price: {}",
                        product.price(),
                        item.price());
                throw new InvalidOrderException("Product price not matching.");
            } else if (item.quantity() < 1) {
                log.error("Order Item has 0 quantity");
                throw new InvalidOrderException("Order Item has 0 quantity");
            }
        }
    }
}
