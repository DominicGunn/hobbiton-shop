package com.hobbiton.shop.products;

import com.hobbiton.shop.config.ExternalServiceConfiguration;
import com.hobbiton.shop.products.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * @author Dominic Gunn
 */
@Component
public class ProductServiceClient {

    private static final String PRODUCT_API_BASE_URL = "https://products-service.herokuapp.com/api/v1/products";
    private static final ParameterizedTypeReference<List<Product>> PRODUCT_TYPE_REFERENCE = new ParameterizedTypeReference<List<Product>>(){ };

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExternalServiceConfiguration externalServiceConfiguration;

    public Product fetchProduct(String productId) {
        final HttpHeaders requestHeaders = createRequestHeaders();
        final String productUrl = PRODUCT_API_BASE_URL + String.format("/%s", productId);
        final ResponseEntity<Product> responseEntity = restTemplate.exchange(
                productUrl, HttpMethod.GET, new HttpEntity<>(requestHeaders), Product.class
        );
        return responseEntity.getBody();
    }

    public List<Product> fetchProducts() {
        final HttpHeaders requestHeaders = createRequestHeaders();
        final ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(
                PRODUCT_API_BASE_URL, HttpMethod.GET, new HttpEntity<>(requestHeaders), PRODUCT_TYPE_REFERENCE
        );
        return responseEntity.getBody();
    }

    private HttpHeaders createRequestHeaders() {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.AUTHORIZATION, String.format("Basic %s",  Base64.getEncoder().encodeToString(String.format("%s:%s",
                externalServiceConfiguration.getProductServiceUsername(),
                externalServiceConfiguration.getProductServicePassword()
        ).getBytes(StandardCharsets.UTF_8))));
        return requestHeaders;
    }
}
