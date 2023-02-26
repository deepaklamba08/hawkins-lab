package org.stranger.common.model.configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * wrapper for configuration
 */
public interface Configuration {

    public Iterable<String> getFieldNames();

    public Configuration getConfiguration(String fieldName);

    public Configuration getConfiguration(String fieldName, Configuration defaultValue);

    public boolean isNull(String fieldName);

    public boolean isPresent(String fieldName);

    public String getString(String fieldName);

    public String getString(String fieldName, String defaultValue);

    public boolean isString(String fieldName);

    public boolean isString();

    public int getInt(String fieldName);

    public int getInt(String fieldName, int defaultValue);

    public boolean isInt(String fieldName);

    public boolean isInt();

    public long getLong(String fieldName);

    public long getLong(String fieldName, long defaultValue);

    public boolean isLong(String fieldName);

    public boolean isLong();

    public float getFloat(String fieldName);

    public float getFloat(String fieldName, float defaultValue);

    public boolean isFloat(String fieldName);

    public boolean isFloat();

    public boolean getBoolean(String fieldName);

    public boolean getBoolean(String fieldName, boolean defaultValue);

    public boolean isBoolean(String fieldName);

    public boolean isBoolean();

    public List<Configuration> getListValue(String fieldName);

    public boolean isArray(String fieldName);

    public boolean isArray();

    public <T> List<T> getListValue(String fieldName, Function<Configuration, T> converter);

    public <T> Map<String, T> getMapValues(String fieldName, Function<Configuration, T> converter);

    public Map<String, Configuration> getMapValues(String fieldName);

    public boolean isObject(String fieldName);

    public boolean isObject();

}
