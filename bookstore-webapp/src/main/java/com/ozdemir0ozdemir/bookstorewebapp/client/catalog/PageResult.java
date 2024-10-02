package com.ozdemir0ozdemir.bookstorewebapp.client.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PageResult<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        @JsonProperty("isFirst") boolean isFirst,
        @JsonProperty("isLast") boolean isLast,
        @JsonProperty("hasNext") boolean hasNext,
        @JsonProperty("hasPrevious") boolean hasPrevious) {}
