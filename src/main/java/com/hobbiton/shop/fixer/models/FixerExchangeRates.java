package com.hobbiton.shop.fixer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

/**
 * @author Dominic Gunn
 */
public class FixerExchangeRates {

    @JsonProperty("base")
    private String baseExchangeRate;

    @JsonProperty("date")
    private Date exchangeDate;

    @JsonProperty("rates")
    private Map<String, Double> exchangeRates;

    public String getBaseExchangeRate() {
        return baseExchangeRate;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public Map<String, Double> getExchangeRates() {
        return exchangeRates;
    }
}
