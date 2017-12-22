package com.hobbiton.shop.products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hobbiton.shop.config.ExternalServiceConfiguration;
import com.hobbiton.shop.products.models.Product;
import com.hobbiton.utils.ResourceAwareTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceClientTest extends ResourceAwareTest {

    private static final String PRODUCT_SERVICE_USERNAME = "test-user";
    private static final String PRODUCT_SERVICE_PASSWORD = "test-password";

    private static final String PRODUCT_ID = "testProductId";
    private static final String PRODUCT_API_BASE_URL = "https://products-service.herokuapp.com/api/v1/products";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExternalServiceConfiguration externalServiceConfiguration;

    @InjectMocks
    private ProductServiceClient productServiceClient;

    @Before
    public void setUp() throws Exception {
        final ResponseEntity collectionResponse = Mockito.mock(ResponseEntity.class);
        final TypeReference<List<Product>> productTypeReference = new TypeReference<List<Product>>() {};
        final List<Product> productList = readResourceCollection("fixtures/mock-product-service-response.json", productTypeReference);

        Mockito.when(collectionResponse.getBody()).thenReturn(productList);
        Mockito.when(restTemplate.exchange(
                Mockito.eq(PRODUCT_API_BASE_URL),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)
        )).thenReturn(collectionResponse);

        final ResponseEntity singleResponse = Mockito.mock(ResponseEntity.class);
        final Product singleProduct = readResource("fixtures/mock-product-service-single-response.json", Product.class);

        Mockito.when(singleResponse.getBody()).thenReturn(singleProduct);
        Mockito.when(restTemplate.exchange(
                Mockito.eq(PRODUCT_API_BASE_URL + String.format("/%s", PRODUCT_ID)),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Product.class)
        )).thenReturn(singleResponse);

        Mockito.when(externalServiceConfiguration.getProductServiceUsername()).thenReturn(PRODUCT_SERVICE_USERNAME);
        Mockito.when(externalServiceConfiguration.getProductServicePassword()).thenReturn(PRODUCT_SERVICE_PASSWORD);
    }

    @Test
    public void testFetchProduct() throws Exception {
        final Product product = productServiceClient.fetchProduct(PRODUCT_ID);
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(PRODUCT_ID);
        assertThat(product.getName()).isEqualTo("testProductName");
        assertThat(product.getUsdPrice()).isEqualTo(1149);
    }

    @Test
    public void testFetchProducts() throws Exception {
        final List<Product> productList = productServiceClient.fetchProducts();

        assertThat(productList).isNotEmpty();
        assertThat(productList).hasSize(9);
    }
}
