package com.hobbiton.shop.fixer;

import com.hobbiton.shop.fixer.exceptions.CurrencyExchangeException;
import com.hobbiton.shop.fixer.models.FixerExchangeRates;
import com.hobbiton.utils.ResourceAwareTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class FixerServiceTest extends ResourceAwareTest {

    private static final String DEFAULT_CURRENCY = "USD";

    @Mock
    private FixerClient fixerClient;

    @InjectMocks
    private FixerService fixerService;

    @Before
    public void setUp() throws Exception {
        final FixerExchangeRates exchangeRates = readResource("fixtures/mock-fixer-api-response.json", FixerExchangeRates.class);
        Mockito.when(fixerClient.fetchExchangeRates(DEFAULT_CURRENCY)).thenReturn(exchangeRates);
    }

    @Test
    public void testGBPExchangeRate() throws Exception {
        final double exchangeValue = fixerService.exchange("GBP", 100);
        assertThat(exchangeValue).isEqualTo(74.79);
    }

    @Test
    public void testAUDExchangeRate() throws Exception {
        final double exchangeValue = fixerService.exchange("AUD", 100);
        assertThat(exchangeValue).isEqualTo(130.50);
    }

    @Test
    public void testUnknownCurrencyThrowsException() throws Exception {
        assertThatThrownBy(() -> fixerService.exchange("NOT_A_CURRENCY", 100))
                .isInstanceOf(CurrencyExchangeException.class)
                .hasMessage("Could not find exchange rate for NOT_A_CURRENCY");
    }
}
