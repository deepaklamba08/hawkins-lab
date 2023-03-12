package org.stranger.common.model.configuration.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.configuration.MutableConfiguration;

import java.util.*;
import java.util.function.Function;

public class JsonConfiguration implements MutableConfiguration {
    private final JsonNode dataNode;

    public JsonConfiguration(JsonNode dataNode) {
        this.dataNode = dataNode;
    }

    @Override
    public Iterator<String> getFieldNames() {
        return this.dataNode.fieldNames();
    }

    @Override
    public Configuration getConfiguration(String fieldName) {
        JsonNode dataNode = this.dataNode.get(fieldName);
        if (dataNode == null) {
            throw new IllegalStateException("value is null for field - " + fieldName);
        }
        return new JsonConfiguration(this.dataNode.get(fieldName));
    }

    @Override
    public Configuration getConfiguration(String fieldName, Configuration defaultValue) {
        JsonNode dataNode = this.dataNode.get(fieldName);
        if (dataNode == null) {
            return defaultValue;
        } else {
            return new JsonConfiguration(this.dataNode.get(fieldName));
        }

    }

    @Override
    public boolean isNull() {
        return this.dataNode == null || this.dataNode.isNull();
    }

    @Override
    public boolean isNull(String fieldName) {
        JsonNode dataNode = this.dataNode.get(fieldName);
        return dataNode == null || dataNode.isNull();
    }

    @Override
    public boolean hasField(String fieldName) {
        return this.dataNode.has(fieldName);
    }

    @Override
    public boolean isPresent(String fieldName) {
        return this.dataNode.has(fieldName);
    }

    @Override
    public String getString(String fieldName) {
        return this.getValue(fieldName, String.class, false, null);
    }

    @Override
    public String getString(String fieldName, String defaultValue) {
        return this.getValue(fieldName, String.class, true, defaultValue);
    }

    @Override
    public boolean isString(String fieldName) {
        return this.isOfType(fieldName, String.class);
    }

    @Override
    public boolean isString() {
        return this.dataNode.isTextual();
    }

    @Override
    public Integer getInt(String fieldName) {
        return this.getValue(fieldName, Integer.class, false, null);
    }

    @Override
    public Integer getInt(String fieldName, int defaultValue) {
        return this.getValue(fieldName, Integer.class, true, defaultValue);
    }

    @Override
    public boolean isInt(String fieldName) {
        return this.isOfType(fieldName, Integer.class);
    }

    @Override
    public boolean isInt() {
        return this.dataNode.isInt();
    }

    @Override
    public Long getLong(String fieldName) {
        return this.getValue(fieldName, Long.class, false, null);
    }

    @Override
    public Long getLong(String fieldName, long defaultValue) {
        return this.getValue(fieldName, Long.class, true, defaultValue);
    }

    @Override
    public boolean isLong(String fieldName) {
        return this.isOfType(fieldName, Long.class);
    }

    @Override
    public boolean isLong() {
        return this.dataNode.isLong();
    }

    @Override
    public Float getFloat(String fieldName) {
        return this.getValue(fieldName, Float.class, false, null);
    }

    @Override
    public Float getFloat(String fieldName, float defaultValue) {
        return this.getValue(fieldName, Float.class, false, defaultValue);
    }

    @Override
    public boolean isFloat(String fieldName) {
        return this.isOfType(fieldName, Float.class);
    }

    @Override
    public boolean isFloat() {
        return this.dataNode.isFloat();
    }

    @Override
    public Boolean getBoolean(String fieldName) {
        return this.getValue(fieldName, Boolean.class, false, null);
    }

    @Override
    public Boolean getBoolean(String fieldName, boolean defaultValue) {
        return this.getValue(fieldName, Boolean.class, true, defaultValue);
    }

    @Override
    public boolean isBoolean(String fieldName) {
        return this.isOfType(fieldName, Boolean.class);
    }

    @Override
    public boolean isBoolean() {
        return this.dataNode.isBoolean();
    }

    @Override
    public Date getDate(String fieldName) {
        return new Date(this.getLong(fieldName));
    }

    @Override
    public Date getDate(String fieldName, Date defaultValue) {
        if (this.isNull(fieldName)) {
            return defaultValue;
        } else {
            return new Date(this.getLong(fieldName));
        }
    }

    @Override
    public List<Configuration> getListValue(String fieldName) {
        return this.getListValue(fieldName, Function.identity());
    }

    @Override
    public boolean isArray(String fieldName) {
        JsonNode value = this.dataNode.get(fieldName);
        if (value == null || value.isNull()) {
            throw new IllegalStateException("data is null");
        }
        return value.isArray();
    }

    @Override
    public boolean isArray() {
        return this.dataNode.isArray();
    }

    @Override
    public <T> List<T> getListValue(String fieldName, Function<Configuration, T> converter) {
        JsonNode value = this.dataNode.get(fieldName);
        return this.getListValue(value, converter);
    }

    @Override
    public List<Configuration> asList() {
        return this.asList(Function.identity());
    }

    @Override
    public <T> List<T> asList(Function<Configuration, T> converter) {
        return this.getListValue(this.dataNode, converter);
    }

    @Override
    public <T> Map<String, T> getMapValues(String fieldName, Function<Configuration, T> converter) {
        JsonNode value = this.dataNode.get(fieldName);
        if (value == null || value.isNull()) {
            throw new IllegalStateException("data is null");
        }
        if (value.isObject()) {
            throw new IllegalStateException("data not an object type");
        }
        Iterator<String> fields = value.fieldNames();
        Map<String, T> valueMap = new HashMap<>();
        while (fields.hasNext()) {
            String field = fields.next();
            valueMap.put(field, converter.apply(new JsonConfiguration(value.get(fieldName))));
        }
        return valueMap;
    }

    @Override
    public Map<String, Configuration> getMapValues(String fieldName) {
        return this.getMapValues(fieldName, Function.identity());
    }

    @Override
    public boolean isObject(String fieldName) {
        return this.isOfType(fieldName, ObjectNode.class);
    }

    @Override
    public boolean isObject() {
        return this.dataNode.isObject();
    }

    @Override
    public void setValue(String field, Configuration value) {
        if (!(value instanceof JsonConfiguration)) {
            throw new IllegalStateException("incompatible type - " + value);
        }
        JsonConfiguration configuration = (JsonConfiguration) value;
        this.getObject().put(field, configuration.dataNode);
    }

    @Override
    public void removeField(String field) {
        if (this.isObject()) {
            ObjectNode node = (ObjectNode) this.dataNode;
            node.remove(field);
        } else {
            throw new UnsupportedOperationException("operation not supported by - " + JsonConfiguration.class);
        }
    }

    @Override
    public void setValue(String field, String value) {
        this.getObject().put(field, value);
    }

    @Override
    public void setValue(String field, int value) {
        this.getObject().put(field, value);
    }

    @Override
    public void setValue(String field, long value) {
        this.getObject().put(field, value);
    }

    @Override
    public void setValue(String field, float value) {
        this.getObject().put(field, value);
    }

    @Override
    public void setValue(String field, boolean value) {
        this.getObject().put(field, value);
    }

    @Override
    public void merge(Configuration other) {
        if (!(other instanceof JsonConfiguration)) {
            throw new IllegalStateException("incompatible type - " + other);
        }
        JsonNode otherDataNode = ((JsonConfiguration) other).dataNode;

        if (other.isArray() && this.isArray()) {
            ArrayNode currArrayNode = (ArrayNode) this.dataNode;
            currArrayNode.addAll((ArrayNode) otherDataNode);
        } else if (other.isObject() && this.isObject()) {
            ObjectNode currObjectNode = (ObjectNode) this.dataNode;
            ObjectNode otherObjectNode = (ObjectNode) otherDataNode;
            Iterator<String> fieldNames = otherObjectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                currObjectNode.put(fieldName, otherObjectNode.get(fieldName));
            }
        } else {
            throw new IllegalStateException("incompatible type can not be merged");
        }


    }

    private <T> T getValue(String fieldName, Class<T> type, boolean useDefaultValue, T defaultValue) {
        if (this.dataNode == null || this.dataNode.isNull()) {
            throw new IllegalStateException("data is null");
        }
        JsonNode value = this.dataNode.get(fieldName);
        if (value == null || value.isNull()) {
            return useDefaultValue ? defaultValue : null;
        }
        if (String.class.equals(type)) {
            return (T) value.textValue();
        } else if (Integer.class.equals(type)) {
            Integer intValue = value.intValue();
            return (T) intValue;
        } else if (Long.class.equals(type)) {
            Long longValue = value.longValue();
            return (T) longValue;
        } else if (Float.class.equals(type)) {
            Float floatValue = value.floatValue();
            return (T) floatValue;
        } else if (Boolean.class.equals(type)) {
            Boolean booleanValue = value.booleanValue();
            return (T) booleanValue;
        } else {
            throw new IllegalStateException("could not fetch value for type - " + type);
        }
    }

    private <T> boolean isOfType(String fieldName, Class<T> type) {
        if (this.dataNode == null || this.dataNode.isNull()) {
            throw new IllegalStateException("data is null");
        }
        JsonNode value = this.dataNode.get(fieldName);
        if (value == null || value.isNull()) {
            throw new IllegalStateException("data is null");
        }
        if (String.class.equals(type)) {
            return value.isTextual();
        } else if (Integer.class.equals(type)) {
            return value.isInt();
        } else if (Long.class.equals(type)) {
            return value.isLong();
        } else if (Float.class.equals(type)) {
            return value.isFloat();
        } else if (Boolean.class.equals(type)) {
            return value.isBoolean();
        } else if (ObjectNode.class.equals(type)) {
            return value.isObject();
        } else {
            throw new IllegalStateException("could not fetch value for type - " + type);
        }
    }

    private ObjectNode getObject() {
        if (this.dataNode == null || this.dataNode.isNull()) {
            throw new IllegalStateException("data is null");
        }
        if (!this.isObject()) {
            throw new IllegalStateException("data is not object type");
        }
        return (ObjectNode) this.dataNode;
    }

    private <T> List<T> getListValue(JsonNode value, Function<Configuration, T> converter) {
        if (value == null || value.isNull()) {
            throw new IllegalStateException("data is null");
        }
        if (!value.isArray()) {
            throw new IllegalStateException("data not an array type");
        }
        List<T> elements = new ArrayList<>();
        value.elements().forEachRemaining(element -> elements.add(converter.apply(new JsonConfiguration(element))));
        return elements;
    }
}
