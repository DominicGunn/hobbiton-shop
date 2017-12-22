package com.hobbiton.shop.web.products;

import com.hobbiton.shop.products.ProductService;
import com.hobbiton.shop.products.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dominic Gunn
 */
@RestController
public class ProductController {

    private static final String DEFAULT_CURRENCY_CODE = "USD";

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(value = "currencyCode", required = false, defaultValue = DEFAULT_CURRENCY_CODE) String currencyCode) {
        final List<Product> productList = productService.getProducts(currencyCode);
        if (productList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productList);
    }

    @RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<Product> getProduct(
            @PathVariable("productId") String productId,
            @RequestParam(value = "currencyCode", required = false, defaultValue = DEFAULT_CURRENCY_CODE) String currencyCode) {
        return ResponseEntity.ok(productService.getProduct(productId, currencyCode));
    }
}
