package org.stranger.common.model;

/**
 * mutable configuration
 */
public interface MutableConfiguration extends Configuration {

    public void setValue(String field, Configuration value);

    public void removeField(String field);

    public void setValue(String field, String value);

    public void setValue(String field, int value);

    public void setValue(String field, long value);

    public void setValue(String field, float value);

    public void setValue(String field, boolean value);

    public void merge(Configuration other);


}
