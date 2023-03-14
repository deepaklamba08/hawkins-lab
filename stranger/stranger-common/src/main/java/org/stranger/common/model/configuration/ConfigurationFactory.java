package org.stranger.common.model.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.configuration.impl.JsonConfiguration;
import org.stranger.common.util.StrangerConstants;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfigurationFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFactory.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static JsonConfiguration load(File source, String type, Optional<Map<String, String>> parameters) throws StrangerExceptions.SystemFailureException, StrangerExceptions.InvalidConfigurationException, StrangerExceptions.ObjectNotFoundException {
        logger.info("Executing : ConfigurationFactory.load(source : {}, type : {}, parameters : {})", source, type, parameters.isPresent());
        if (source == null || type == null || type.isEmpty()) {
            logger.error("Invalid parameters, source : {}, type : {}", source, type);
            throw new IllegalArgumentException("Invalid parameters, source : " + source + ", type : " + type);
        }

        if (!source.exists()) {
            throw new StrangerExceptions.ObjectNotFoundException("source does not exists, source - " + source.getAbsolutePath());
        }

        if (StrangerConstants.REPO_TYPE_JSON.equalsIgnoreCase(type)) {
            return loadJsonConfiguration(source, parameters);
        } else {
            throw new StrangerExceptions.InvalidConfigurationException("configuration type not supported - " + type);
        }
    }

    private static JsonConfiguration loadJsonConfiguration(File source, Optional<Map<String, String>> parameters) throws StrangerExceptions.SystemFailureException, StrangerExceptions.InvalidConfigurationException {
        if (parameters.isPresent()) {
            String configStr = loadFile(source);
            try {
                JsonNode config = OBJECT_MAPPER.readTree(resolve(configStr, configStr, parameters.get()));
                return new JsonConfiguration(config);
            } catch (IOException e) {
                throw new StrangerExceptions.SystemFailureException("error occurred while parsing file - " + source, e);
            }

        } else {
            try {
                JsonNode config = OBJECT_MAPPER.readTree(source);
                return new JsonConfiguration(config);
            } catch (IOException e) {
                throw new StrangerExceptions.SystemFailureException("error occurred while parsing file - " + source, e);
            }
        }
    }

    private static String loadFile(File source) throws StrangerExceptions.SystemFailureException {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(source));
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (FileNotFoundException e) {
            throw new StrangerExceptions.SystemFailureException("error occurred while reading file - " + source, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new StrangerExceptions.SystemFailureException("error occurred while reading file - " + source, e);
                }
            }
        }
    }

    private static String resolve(String content, String type, Map<String, String> parameters) throws StrangerExceptions.InvalidConfigurationException {
        logger.info("resolving configuration ...");
        if (StrangerConstants.REPO_TYPE_JSON.equalsIgnoreCase(type)) {
            return content;
        } else {
            throw new StrangerExceptions.InvalidConfigurationException("configuration type not supported - " + type);
        }
    }
}
