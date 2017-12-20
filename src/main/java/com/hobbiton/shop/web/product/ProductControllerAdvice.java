package com.hobbiton.shop.web.product;

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
public class ProductControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ApiError handleProductNotFoundException(ProductNotFoundException productNotFoundException) {
        return new ApiError(productNotFoundException.getMessage());
    }
}
