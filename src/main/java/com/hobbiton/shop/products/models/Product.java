package com.hobbiton.shop.products.models;

/**
 * @author Dominic Gunn
 */
public class Product {

    private String id;
    private String name;

    private int usdPrice;
    private double price;

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
