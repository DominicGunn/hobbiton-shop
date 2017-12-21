package com.hobbiton.shop.persistence.packages.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hobbiton.shop.products.models.Product;

import java.util.List;

/**
 * @author Dominic Gunn
 */
public class ShopPackage {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty("products")
    private List<Product> productList;

    public ShopPackage(Long id, String name, String description, List<Product> productList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.productList = productList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public double getPrice() {
        return productList.stream().mapToDouble(Product::getPrice).sum();
    }
}
