package com.hobbiton.shop.fixer;

import com.hobbiton.shop.fixer.exceptions.CurrencyExchangeException;
import com.hobbiton.shop.fixer.models.FixerExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Dominic Gunn
 */
@Service
public class FixerService {

    private static final String DEFAULT_CURRENCY = "USD";

    @Autowired
    private FixerClient fixerClient;

    public double exchange(String requestedCurrency, double currentPrice) {
        final BigDecimal exchangeRate = fetchExchangeRate(DEFAULT_CURRENCY, requestedCurrency);
        final BigDecimal newPrice = exchangeRate.multiply(BigDecimal.valueOf(currentPrice));
        return newPrice.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
    }

    private BigDecimal fetchExchangeRate(String currentCurrency, String exchangeCurrency) {
        final FixerExchangeRates fixerExchangeRates = fixerClient.fetchExchangeRates(currentCurrency);
        final Double exchangeRate = fixerExchangeRates.getExchangeRates().get(exchangeCurrency);
        if (exchangeRate == null) {
            throw new CurrencyExchangeException(String.format("Could not find exchange rate for %s", exchangeCurrency));
        }
        return BigDecimal.valueOf(exchangeRate);
    }
}
