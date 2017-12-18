package com.hobbiton.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dominic Gunn
 */
@RunWith(MockitoJUnitRunner.class)
public class RestTemplateConfigurationTest {

    @InjectMocks
    private RestTemplateConfiguration restTemplateConfiguration;

    @Test
    public void testRestTemplateIsNotNull() throws Exception {
        assertThat(restTemplateConfiguration).isNotNull();
    }
}
