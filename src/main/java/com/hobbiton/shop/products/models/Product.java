package com.hobbiton.shop.products.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Dominic Gunn
 */
public class Product {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private double price;

    private transient int usdPrice;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
