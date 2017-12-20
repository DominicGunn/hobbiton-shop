package com.hobbiton.shop.products.models;

/**
 * @author Dominic Gunn
 */
public class Product {

    private String id;
    private String name;
    private int usdPrice;

    public Product() {
        // Default Constructor
    }

    public Product(String id, String name, int usdPrice) {
        this.id = id;
        this.name = name;
        this.usdPrice = usdPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUsdPrice() {
        return usdPrice;
    }
}
