package com.hobbiton.shop.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class ExternalServiceConfigurationTest {

    @InjectMocks
    private ExternalServiceConfiguration externalServiceConfiguration;

    @Before
    public void setUp() throws Exception {
        Whitebox.setInternalState(externalServiceConfiguration, "productServiceUsername", "testUsername");
        Whitebox.setInternalState(externalServiceConfiguration, "productServicePassword", "testPassword");
        Whitebox.setInternalState(externalServiceConfiguration, "productServiceAvailable", true);
        Whitebox.setInternalState(externalServiceConfiguration, "productServiceUrl", "http://product-service");
    }

    @Test
    public void testShouldReturnExternalServiceUsername() throws Exception {
        assertThat(externalServiceConfiguration.getProductServiceUsername()).isEqualTo("testUsername");
    }

    @Test
    public void testShouldReturnExternalServicePassword() throws Exception {
        assertThat(externalServiceConfiguration.getProductServicePassword()).isEqualTo("testPassword");
    }

    @Test
    public void testShouldReturnExternalServiceAvailabilityFlag() throws Exception {
        assertThat(externalServiceConfiguration.isProductServiceAvailable()).isTrue();
    }

    @Test
    public void testShouldReturnExternalServiceUrl() throws Exception {
        assertThat(externalServiceConfiguration.getProductServiceUrl()).isEqualTo("http://product-service");
    }
}
