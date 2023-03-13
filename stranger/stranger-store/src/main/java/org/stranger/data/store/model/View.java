package org.stranger.data.store.model;

import java.util.List;
import java.util.Optional;

public class View {

    private final String name;
    private final String type;
    private final boolean isPersist;
    private final String persistMode;
    private final Optional<DataRestructure> dataRestructure;

    public View(String name, String type, boolean isPersist, String persistMode, Optional<DataRestructure> dataRestructure) {
        this.name = name;
        this.type = type;
        this.isPersist = isPersist;
        this.persistMode = persistMode;
        this.dataRestructure = dataRestructure;
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

    public Optional<DataRestructure> getDataRestructure() {
        return dataRestructure;
    }

    public static class DataRestructure {
        private final String restructureOn;
        private final int numPartitions;
        private final List<String> columns;

        public DataRestructure(String restructureOn, int numPartitions, List<String> columns) {
            this.restructureOn = restructureOn;
            this.numPartitions = numPartitions;
            this.columns = columns;
        }

        public String getRestructureOn() {
            return restructureOn;
        }

        public int getNumPartitions() {
            return numPartitions;
        }

        public List<String> getColumns() {
            return columns;
        }
    }
}
