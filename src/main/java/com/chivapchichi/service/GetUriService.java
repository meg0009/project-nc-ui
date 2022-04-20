package com.chivapchichi.service;

import org.springframework.beans.factory.annotation.Value;

public class GetUriService {

    @Value("${api.nameserver}")
    private String localhost;

    public String getUri(String path) {
        return localhost + path;
    }
}
