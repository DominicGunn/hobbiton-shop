package com.hobbiton.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dominic Gunn
 */
@Configuration
public class ExternalServiceConfiguration {

    @Value("${external.product.service.username:user}")
    private String productServiceUsername;

    @Value("${external.product.service.password:pass}")
    private String productServicePassword;

    @Value("${external.product.service.available:true}")
    private boolean productServiceAvailable;

    @Value("${external.product.service.url:https://products-service.herokuapp.com/api/v1/products")
    private String productServiceUrl;

    public String getProductServiceUsername() {
        return productServiceUsername;
    }

    public String getProductServicePassword() {
        return productServicePassword;
    }

    public boolean isProductServiceAvailable() {
        return productServiceAvailable;
    }

    public String getProductServiceUrl() {
        return productServiceUrl;
    }
}
