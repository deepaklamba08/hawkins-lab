package org.stranger.common.model;

public enum AppType {
    BATCH;

    public static AppType of(String type) {
        if (BATCH.name().equalsIgnoreCase(type)) {
            return AppType.BATCH;
        } else {
            throw new IllegalStateException("invalid aap type - " + type);
        }
    }

}
