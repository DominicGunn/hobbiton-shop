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
import java.util.Arrays;
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
        if (externalServiceConfiguration.getProductServiceAvailable()) {
            final HttpHeaders requestHeaders = createRequestHeaders();
            final ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(
                    PRODUCT_API_BASE_URL, HttpMethod.GET, new HttpEntity<>(requestHeaders), PRODUCT_TYPE_REFERENCE
            );
            return responseEntity.getBody();
        }
        return mockProductList();
    }

    private HttpHeaders createRequestHeaders() {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.AUTHORIZATION, String.format("Basic %s",  Base64.getEncoder().encodeToString(String.format("%s:%s",
                externalServiceConfiguration.getProductServiceUsername(),
                externalServiceConfiguration.getProductServicePassword()
        ).getBytes(StandardCharsets.UTF_8))));
        return requestHeaders;
    }

    /**
     * For the majority of writing this, the product service:
     * https://products-service.herokuapp.com/api/v1/products
     * was offline. This was a hack so development could continue.
     * @return A list of Products.
     */
    private List<Product> mockProductList() {
        return Arrays.asList(
                new Product("VqKb4tyj9V6i", "Shield", 1149),
                new Product("DXSQpv6XVeJm", "Helmet", 999),
                new Product("7dgX6XzU3Wds", "Sword", 899),
                new Product("PKM5pGAh9yGm", "Axe", 799),
                new Product("7Hv0hA2nmci7", "Knife", 349),
                new Product("500R5EHvNlNB", "Gold Coin", 249),
                new Product("IP3cv7TcZhQn", "Platinum Cold", 399),
                new Product("IJOHGYkY2CYq", "Bow", 649),
                new Product("8anPsR2jbfNW", "Silver Coin", 50)
        );
    }
}
