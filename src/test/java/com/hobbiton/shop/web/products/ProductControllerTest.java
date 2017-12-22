package com.hobbiton.shop.web.products;

import com.hobbiton.shop.products.ProductService;
import com.hobbiton.shop.products.exceptions.ProductNotFoundException;
import com.hobbiton.shop.products.models.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    private static final String DEFAULT_CURRENCY_CODE = "USD";

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        final Product testProduct = new Product("testProductId", "productName", 100);
        final Product testProduct2 = new Product("testProductId2", "productName2", 150);

        // This conversion would happen within the mocked product services call to the FixerService.
        testProduct.setPrice(100);
        testProduct2.setPrice(150);

        Mockito.when(productService.getProduct("testProductId", DEFAULT_CURRENCY_CODE))
                .thenReturn(testProduct);

        Mockito.when(productService.getProduct("doesNotExist", DEFAULT_CURRENCY_CODE))
                .thenThrow(new ProductNotFoundException("Product doesNotExist does not exist!"));

        Mockito.when(productService.getProducts(DEFAULT_CURRENCY_CODE)).thenReturn(Arrays.asList(testProduct, testProduct2));

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ProductControllerAdvice())
                .build();
    }

    @Test
    public void testShouldReturnProductList() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value("testProductId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value("testProductId2"));
    }

    @Test
    public void testShouldReturnProduct() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/testProductId"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value("100.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.usdPrice").value("100"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("testProductId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("productName"));
    }

    @Test
    public void testShouldReturnErrorMessageIfProductNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/doesNotExist"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Product doesNotExist does not exist!"));
    }
}
