package org.stranger.common.model.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.stranger.common.model.configuration.impl.JsonConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigurationFactory {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonConfiguration load(File source, String type) {

        if ("json".equalsIgnoreCase(type)) {
            try {
                JsonNode config = OBJECT_MAPPER.readTree(source);
                return new JsonConfiguration(config);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("configuration type not supported - " + type);
        }
    }
}
