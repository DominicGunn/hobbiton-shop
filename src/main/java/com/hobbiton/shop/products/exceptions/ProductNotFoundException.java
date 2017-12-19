package com.hobbiton.shop.products.exceptions;

/**
 * @author Dominic Gunn
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
