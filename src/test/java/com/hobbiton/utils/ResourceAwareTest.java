package com.hobbiton.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Dominic Gunn
 */
public abstract class ResourceAwareTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClassLoader classLoader = ResourceAwareTest.class.getClassLoader();

    public <T> T readResource(String resourceLocation, Class<T> clazz) throws Exception {
        final Path filePath = Paths.get(classLoader.getResource(resourceLocation).toURI());
        final byte[] resourceContents = Files.readAllBytes(filePath);
        return objectMapper.readValue(resourceContents, clazz);
    }
}
