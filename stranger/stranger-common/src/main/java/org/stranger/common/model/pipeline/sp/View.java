package org.stranger.common.model.pipeline.sp;

public class View {

    private final String name;
    private final String type;
    private final boolean isPersist;
    private final String persistMode;

    public View(String name, String type, boolean isPersist, String persistMode) {
        this.name = name;
        this.type = type;
        this.isPersist = isPersist;
        this.persistMode = persistMode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isPersist() {
        return isPersist;
    }

    public String getPersistMode() {
        return persistMode;
    }
}
