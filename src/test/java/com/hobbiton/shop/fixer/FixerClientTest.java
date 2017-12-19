package com.hobbiton.shop.fixer;

import com.hobbiton.shop.fixer.models.FixerExchangeRates;
import com.hobbiton.utils.ResourceAwareTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class FixerClientTest extends ResourceAwareTest {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final String EXCHANGE_CURRENCY = "USD";
    private static final Double GBP_EXCHANGE_RATE = 0.74784;
    private static final String FIXER_API_URL = "https://api.fixer.io/latest?base=%s";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FixerClient fixerClient;

    @Before
    public void setUp() throws Exception {
        final FixerExchangeRates exchangeRates = readResource("fixtures/mock-fixer-api-response.json", FixerExchangeRates.class);
        Mockito.when(restTemplate.getForObject(String.format(FIXER_API_URL, EXCHANGE_CURRENCY), FixerExchangeRates.class)).thenReturn(exchangeRates);
    }

    @Test
    public void testFetchExchangeRates() throws Exception {
        final FixerExchangeRates exchangeRates = fixerClient.fetchExchangeRates(EXCHANGE_CURRENCY);

        final Date exchangeDate = SIMPLE_DATE_FORMAT.parse("2017-12-18");
        assertThat(exchangeRates.getExchangeDate()).isEqualTo(exchangeDate);
        assertThat(exchangeRates.getBaseExchangeRate()).isEqualTo(EXCHANGE_CURRENCY);
        assertThat(exchangeRates.getExchangeRates().get("GBP")).isEqualTo(GBP_EXCHANGE_RATE);
    }
}
