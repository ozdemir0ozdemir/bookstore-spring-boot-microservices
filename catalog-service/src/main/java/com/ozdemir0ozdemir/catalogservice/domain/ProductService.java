package com.ozdemir0ozdemir.catalogservice.domain;

import com.ozdemir0ozdemir.catalogservice.ApplicationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public PageResult<Product> getProducts(int pageNo, int pageSize) {

        int checkedNumber = pageNo <= 1 ? 0 : pageNo - 1;

        Page<Product> productsPage = repository
                .findAll(PageRequest.of(checkedNumber, pageSize, Sort.by("code").ascending()))
                .map(Product::from);

        return new PageResult<>(
                productsPage.getContent(),
                productsPage.getTotalElements(),
                productsPage.getNumber() + 1,
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                productsPage.hasNext(),
                productsPage.hasPrevious()
        );
    }

    public Optional<Product> getProductByCode(String code){
        return repository.findByCode(code).map(Product::from);
    }
}
