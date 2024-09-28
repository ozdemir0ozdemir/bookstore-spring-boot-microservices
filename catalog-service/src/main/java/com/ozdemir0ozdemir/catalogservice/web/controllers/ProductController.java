package com.ozdemir0ozdemir.catalogservice.web.controllers;

import com.ozdemir0ozdemir.catalogservice.domain.PageResult;
import com.ozdemir0ozdemir.catalogservice.domain.Product;
import com.ozdemir0ozdemir.catalogservice.domain.ProductService;
import com.ozdemir0ozdemir.catalogservice.domain.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public PageResult<Product> getProducts(
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        return service.getProducts(pageNo, pageSize);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Product> getProduct(@PathVariable String code){
        return service.getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
