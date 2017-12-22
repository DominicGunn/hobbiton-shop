package com.hobbiton.shop.web.packages;

import com.hobbiton.shop.persistence.packages.exceptions.PackageNotFoundException;
import com.hobbiton.shop.products.exceptions.ProductNotFoundException;
import com.hobbiton.shop.web.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Dominic Gunn
 */
@RestControllerAdvice
public class PackageControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PackageNotFoundException.class)
    public ApiError handlePackageNotFoundException(PackageNotFoundException packageNotFoundException) {
        return new ApiError(packageNotFoundException.getMessage());
    }
}
