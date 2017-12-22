package com.hobbiton.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dominic Gunn
 */
@Configuration
public class ExternalServiceConfiguration {

    @Value("${external.products.service.username:user}")
    private String productServiceUsername;

    @Value("${external.products.service.password:pass}")
    private String productServicePassword;

    @Value("${external.products.service.available:true}")
    private boolean productServiceAvailable;

    public String getProductServiceUsername() {
        return productServiceUsername;
    }

    public String getProductServicePassword() {
        return productServicePassword;
    }

    public boolean getProductServiceAvailable() {
        return productServiceAvailable;
    }
}
