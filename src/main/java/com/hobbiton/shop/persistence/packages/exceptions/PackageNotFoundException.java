package com.hobbiton.shop.persistence.packages.exceptions;

/**
 * @author Dominic Gunn
 */
public class PackageNotFoundException extends RuntimeException {

    public PackageNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
