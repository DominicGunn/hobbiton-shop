package com.hobbiton.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Dominic Gunn
 */
public abstract class ResourceAwareTest {

    protected static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ClassLoader classLoader = ResourceAwareTest.class.getClassLoader();

    public <T> T readResource(String resourceLocation, Class<T> clazz) throws Exception {
        return objectMapper.readValue(getResourceContents(resourceLocation), clazz);
    }

    public <T> T readResourceCollection(String resourceLocation, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(getResourceContents(resourceLocation), typeReference);
    }

    private byte[] getResourceContents(String resourceLocation) throws Exception {
        final Path filePath = Paths.get(classLoader.getResource(resourceLocation).toURI());
        return Files.readAllBytes(filePath);
    }
}
