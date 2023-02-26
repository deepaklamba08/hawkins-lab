package org.stranger.common.model.user;

public class Credential {
    private final String value;

    public Credential(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[value=********]";
    }
}
