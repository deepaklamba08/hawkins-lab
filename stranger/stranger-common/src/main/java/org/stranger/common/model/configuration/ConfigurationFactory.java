package org.stranger.common.model.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.stranger.common.model.configuration.impl.JsonConfiguration;
import org.stranger.common.util.StrangerConstants;

import java.io.File;
import java.io.IOException;

public class ConfigurationFactory {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static JsonConfiguration load(File source, String type) {

        if (StrangerConstants.REPO_TYPE_JSON.equalsIgnoreCase(type)) {
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
