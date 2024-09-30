package com.ozdemir0ozdemir.orderservice.client.catalog;

import java.math.BigDecimal;

public record Product(String code, String name, String description, String imageUrl, BigDecimal price) {}
