package com.ozdemir0ozdemir.bookstorewebapp.web.controller;

import com.ozdemir0ozdemir.bookstorewebapp.client.catalog.CatalogServiceClient;
import com.ozdemir0ozdemir.bookstorewebapp.client.catalog.PageResult;
import com.ozdemir0ozdemir.bookstorewebapp.client.catalog.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final CatalogServiceClient catalogService;

    ProductController(CatalogServiceClient catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    String productsPage(@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber, Model model) {
        model.addAttribute("pageNumber", pageNumber);
        return "products";
    }

    // REST API BRIDGE
    @GetMapping("/api/products")
    @ResponseBody
    PageResult<Product> getAllProducts(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        log.info("Fetching products for page: {}", page);
        return catalogService.getProducts(page);
    }
}
