package com.hobbiton.shop.web.packages.models;

import java.util.List;

/**
 * @author Dominic Gunn
 */
public class PackageRequest {

    private String name;
    private String description;
    private List<String> productIds;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getProductIds() {
        return productIds;
    }
}
