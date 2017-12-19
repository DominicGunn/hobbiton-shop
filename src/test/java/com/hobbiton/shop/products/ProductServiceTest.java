package com.hobbiton.shop.products;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hobbiton.shop.products.exceptions.ProductNotFoundException;
import com.hobbiton.shop.products.models.Product;
import com.hobbiton.utils.ResourceAwareTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest extends ResourceAwareTest {

    private static final String PRODUCT_ID = "testProductId";
    private static final String CACHED_PRODUCT_ID = "VqKb4tyj9V6i";
    private static final String ERROR_PRODUCT_ID = "IAMERROR";

    @Mock
    private ProductServiceClient productServiceClient;

    @InjectMocks
    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        final Product singleProduct = readResource("fixtures/mock-product-service-single-response.json", Product.class);
        Mockito.when(productServiceClient.fetchProduct(PRODUCT_ID)).thenReturn(singleProduct);

        final TypeReference<List<Product>> productTypeReference = new TypeReference<List<Product>>() {};
        final List<Product> productList = readResourceCollection("fixtures/mock-product-service-response.json", productTypeReference);
        Mockito.when(productServiceClient.fetchProducts()).thenReturn(productList);
    }

    @Test
    public void testShouldFetchAndCacheProducts() throws Exception {
        final List<Product> productList = productService.getProducts();

        assertThat(productList).isNotNull();
        assertThat(productList).hasSize(9);

        Mockito.verify(productServiceClient, Mockito.times(1)).fetchProducts();
    }

    @Test
    public void testShouldFetchProductsOnce() throws Exception {
        for (int i = 0; i < 2; i++) {
            productService.getProducts();
        }
        Mockito.verify(productServiceClient, Mockito.times(1)).fetchProducts();
    }

    @Test
    public void testShouldFetchAndCacheProductNotInitiallyCached() throws Exception {
        final List<Product> productList = productService.getProducts();
        final Product product = productService.getProduct(PRODUCT_ID);

        assertThat(productList).isNotNull();
        assertThat(productList).hasSize(9);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(PRODUCT_ID);

        Mockito.verify(productServiceClient, Mockito.times(1)).fetchProducts();
        Mockito.verify(productServiceClient, Mockito.times(1)).fetchProduct(PRODUCT_ID);
    }

    @Test
    public void testShouldNotFetchCachedProduct() throws Exception {
        final List<Product> productList = productService.getProducts();
        final Product product = productService.getProduct(CACHED_PRODUCT_ID);

        assertThat(productList).isNotNull();
        assertThat(productList).hasSize(9);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(CACHED_PRODUCT_ID);

        Mockito.verify(productServiceClient, Mockito.times(1)).fetchProducts();
        Mockito.verify(productServiceClient, Mockito.never()).fetchProduct(CACHED_PRODUCT_ID);
    }

    @Test
    public void testProductNotFoundExceptionIsThrownIfProductDoesNotExist() throws Exception {
        Mockito.when(productServiceClient.fetchProduct(ERROR_PRODUCT_ID)).thenThrow(new RestClientException("Errors!"));

        assertThatThrownBy(() -> productService.getProduct(ERROR_PRODUCT_ID))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product IAMERROR not found!");
    }
}
