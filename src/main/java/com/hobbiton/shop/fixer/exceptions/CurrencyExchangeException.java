package com.hobbiton.shop.fixer.exceptions;

/**
 * @author Dominic Gunn
 */
public class CurrencyExchangeException extends RuntimeException {

    public CurrencyExchangeException(String errorMessage) {
        super(errorMessage);
    }
}
