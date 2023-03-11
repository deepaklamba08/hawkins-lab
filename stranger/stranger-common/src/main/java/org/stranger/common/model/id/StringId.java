package org.stranger.common.model.id;

import java.util.Objects;

public class StringId implements Id {
    private final String value;

    public StringId(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringId stringId = (StringId) o;
        return Objects.equals(value, stringId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "StringId[ " + value + " ]";
    }
}
