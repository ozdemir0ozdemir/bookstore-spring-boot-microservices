package com.ozdemir0ozdemir.orderservice.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoginUsername() {
        return "user";
    }
}
