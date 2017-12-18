package com.hobbiton.shop.fixer;

import com.hobbiton.shop.fixer.models.FixerExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Dominic Gunn
 */
@Component
public class FixerClient {

    private static final String FIXER_API_URL = "https://api.fixer.io/latest?base=%s";

    @Autowired
    private RestTemplate restTemplate;

    public FixerExchangeRates fetchExchangeRates(String baseExchangeRate) {
        return restTemplate.getForObject(String.format(FIXER_API_URL, baseExchangeRate), FixerExchangeRates.class);
    }
}
