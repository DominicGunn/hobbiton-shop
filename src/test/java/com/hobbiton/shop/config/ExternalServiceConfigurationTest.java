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
    }

    @Test
    public void testShouldReturnExternalServiceUsername() throws Exception {
        assertThat(externalServiceConfiguration.getProductServiceUsername()).isEqualTo("testUsername");
    }

    @Test
    public void testShouldReturnExternalServicePassword() throws Exception {
        assertThat(externalServiceConfiguration.getProductServicePassword()).isEqualTo("testPassword");
    }
}
