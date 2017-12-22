package com.hobbiton.shop.web.packages;

import com.hobbiton.shop.persistence.packages.PackageService;
import com.hobbiton.shop.persistence.packages.exceptions.PackageNotFoundException;
import com.hobbiton.shop.persistence.packages.models.ShopPackage;
import com.hobbiton.shop.products.models.Product;
import com.hobbiton.shop.web.packages.models.PackageRequest;
import com.hobbiton.utils.ResourceAwareTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class PackageControllerTest extends ResourceAwareTest {

    private static final String DEFAULT_CURRENCY_CODE = "USD";

    @Mock
    private PackageService packageService;

    @InjectMocks
    private PackageController packageController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        final Product firstProduct = new Product("testProductId", "productName", 100);
        final Product secondProduct = new Product("testProductId2", "productName2", 150);

        final ShopPackage firstPackage = new ShopPackage(1L, "Test Package", "Test Description", Arrays.asList(firstProduct, secondProduct));
        final ShopPackage secondPackage = new ShopPackage(2L, "Second Package", "Test Description", Collections.singletonList(firstProduct));
        final ShopPackage updatedPackage = new ShopPackage(1L, "Updated Package", "Test Description", Collections.singletonList(firstProduct));

        Mockito.when(packageService.getPackage(1L, DEFAULT_CURRENCY_CODE)).thenReturn(firstPackage);
        Mockito.when(packageService.getPackage(10L, DEFAULT_CURRENCY_CODE)).thenThrow(new PackageNotFoundException("Could not find package 10"));

        Mockito.when(packageService.getPackages(DEFAULT_CURRENCY_CODE)).thenReturn(Arrays.asList(firstPackage, secondPackage));

        Mockito.when(
                packageService.savePackage("Test Package", "Test Description", Arrays.asList("testProductId", "testProductId2"), DEFAULT_CURRENCY_CODE)
        ).thenReturn(firstPackage);

        Mockito.when(
                packageService.updatePackage(1L, "Updated Package", "Test Description", Collections.singletonList("testProductId"), DEFAULT_CURRENCY_CODE)
        ).thenReturn(updatedPackage);

        mockMvc = MockMvcBuilders.standaloneSetup(packageController)
                .setControllerAdvice(new PackageControllerAdvice())
                .build();
    }

    @Test
    public void testShouldReturnListOfPackages() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/packages"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Test Package"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Second Package"));

        Mockito.verify(packageService, Mockito.times(1)).getPackages(DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldReturnSelectedPackage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/packages/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Package"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products.[0].id").value("testProductId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products.[1].id").value("testProductId2"));

        Mockito.verify(packageService, Mockito.times(1)).getPackage(1L, DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldReturnErrorMessageIfPackageNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/packages/10"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Could not find package 10"));
    }

    @Test
    public void testShouldSavePackage() throws Exception {
        final PackageRequest packageRequest = readResource("fixtures/mock-package-creation.json", PackageRequest.class);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/packages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(packageRequest));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/packages/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Package"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products.[0].id").value("testProductId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products.[1].id").value("testProductId2"));

        Mockito.verify(packageService, Mockito.times(1))
                .savePackage("Test Package", "Test Description", Arrays.asList("testProductId", "testProductId2"), DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldUpdatePackage() throws Exception {
        final PackageRequest packageRequest = readResource("fixtures/mock-package-update.json", PackageRequest.class);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/packages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(packageRequest));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Package"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products.[0].id").value("testProductId"));

        Mockito.verify(packageService, Mockito.times(1))
                .updatePackage(1L, "Updated Package", "Test Description", Collections.singletonList("testProductId"), DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldDeletePackage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/packages/1"));
        Mockito.verify(packageService, Mockito.times(1)).deletePackage(1L);
    }
}
