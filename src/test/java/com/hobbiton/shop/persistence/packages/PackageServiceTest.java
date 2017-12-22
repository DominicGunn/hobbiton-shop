package com.hobbiton.shop.persistence.packages;

import com.hobbiton.shop.persistence.packages.exceptions.PackageNotFoundException;
import com.hobbiton.shop.persistence.packages.models.ShopPackage;
import com.hobbiton.shop.products.ProductService;
import com.hobbiton.shop.products.models.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class PackageServiceTest {

    private static final String DEFAULT_CURRENCY_CODE = "GBP";

    @Mock
    private ProductService productService;

    @Mock
    private PackageRepository packageRepository;

    @InjectMocks
    private PackageService packageService;

    private PackageDto packageDto;

    @Before
    public void setUp() throws Exception {
        final Product firstProduct = new Product("testProductId", "productName", 100);
        firstProduct.setPrice(100);

        this.packageDto = new PackageDto("Test Package", "Test Description", Collections.singletonList("testProduct"));

        Mockito.when(productService.getProduct("testProduct", DEFAULT_CURRENCY_CODE)).thenReturn(firstProduct);

        Mockito.when(packageRepository.findOne(0L)).thenReturn(null);
        Mockito.when(packageRepository.findOne(1L)).thenReturn(packageDto);
        Mockito.when(packageRepository.findAll()).thenReturn(Collections.singleton(packageDto));

        Mockito.when(packageRepository.save(any(PackageDto.class))).thenReturn(packageDto);
    }

    @Test
    public void testShouldReturnShopPackageById() throws Exception {
        final ShopPackage shopPackage = packageService.getPackage(1L, DEFAULT_CURRENCY_CODE);
        assertThat(shopPackage.getPrice()).isEqualTo(100);
        assertThat(shopPackage.getName()).isEqualTo("Test Package");
        assertThat(shopPackage.getDescription()).isEqualTo("Test Description");

        final Product product = shopPackage.getProductList().get(0);
        assertThat(product.getName()).isEqualTo("productName");

        Mockito.verify(packageRepository, Mockito.times(1)).findOne(1L);
        Mockito.verify(productService, Mockito.times(1)).getProduct("testProduct", DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldThrowPackageNotFound() throws Exception {
        assertThatThrownBy(() -> packageService.getPackage(0L, DEFAULT_CURRENCY_CODE))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessage("Package 0 was not found");
    }

    @Test
    public void testShouldReturnCollectionOfProducts() throws Exception {
        final List<ShopPackage> shopPackageList = packageService.getPackages(DEFAULT_CURRENCY_CODE);
        assertThat(shopPackageList).hasSize(1);

        final ShopPackage shopPackage = shopPackageList.get(0);
        assertThat(shopPackage.getPrice()).isEqualTo(100);
        assertThat(shopPackage.getName()).isEqualTo("Test Package");
        assertThat(shopPackage.getDescription()).isEqualTo("Test Description");

        final Product product = shopPackage.getProductList().get(0);
        assertThat(product.getName()).isEqualTo("productName");

        Mockito.verify(packageRepository, Mockito.times(1)).findAll();
        Mockito.verify(productService, Mockito.times(1)).getProduct("testProduct", DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldSavePackage() throws Exception {
        final ShopPackage shopPackage = packageService.savePackage("Test Package", "Test Description", Collections.singletonList("testProductId"), DEFAULT_CURRENCY_CODE);
        assertThat(shopPackage.getPrice()).isEqualTo(100);
        assertThat(shopPackage.getName()).isEqualTo("Test Package");
        assertThat(shopPackage.getDescription()).isEqualTo("Test Description");

        final Product product = shopPackage.getProductList().get(0);
        assertThat(product.getName()).isEqualTo("productName");

        Mockito.verify(packageRepository, Mockito.times(1)).save(any(PackageDto.class));
        Mockito.verify(productService, Mockito.times(1)).getProduct("testProduct", DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldUpdatePackage() throws Exception {
        final ShopPackage shopPackage = packageService.updatePackage(1L, "Test Package", "Test Description", Collections.singletonList("testProduct"), DEFAULT_CURRENCY_CODE);
        assertThat(shopPackage.getPrice()).isEqualTo(100);
        assertThat(shopPackage.getName()).isEqualTo("Test Package");
        assertThat(shopPackage.getDescription()).isEqualTo("Test Description");

        final Product product = shopPackage.getProductList().get(0);
        assertThat(product.getName()).isEqualTo("productName");

        Mockito.verify(packageRepository, Mockito.times(1)).findOne(1L);
        Mockito.verify(packageRepository, Mockito.times(1)).save(packageDto);
        Mockito.verify(productService, Mockito.times(1)).getProduct("testProduct", DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void testShouldThrowPackageNotFoundExceptionIfUpdatingPackageThatDoesNotExist() throws Exception {
        assertThatThrownBy(() -> packageService.updatePackage(0L, "Test Package", "Test Description", Collections.singletonList("testProduct"), DEFAULT_CURRENCY_CODE))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessage("Package 0 could not be updated because it was not found");
    }

    @Test
    public void testShouldDeletePackage() throws Exception {
        packageService.deletePackage(1L);

        Mockito.verify(packageRepository, Mockito.times(1)).findOne(1L);
        Mockito.verify(packageRepository, Mockito.times(1)).delete(packageDto);
    }

    @Test
    public void testShouldThrowPackageNotFoundExceptionIfDeletingPackageThatDoesNotExist() throws Exception {
        assertThatThrownBy(() -> packageService.deletePackage(0L))
                .isInstanceOf(PackageNotFoundException.class)
                .hasMessage("Could not delete package 0, it does not exist");
    }
}
