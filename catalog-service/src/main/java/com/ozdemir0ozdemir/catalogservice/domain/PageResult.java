package com.ozdemir0ozdemir.catalogservice.domain;

import java.util.List;

public record PageResult<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {
}
