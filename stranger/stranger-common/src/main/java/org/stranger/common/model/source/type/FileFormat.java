package org.stranger.common.model.source.type;


public enum FileFormat {
    CSV, PARQUET, ORC, AVRO;

    public static FileFormat of(String type) {
        if (CSV.name().equalsIgnoreCase(type)) {
            return FileFormat.CSV;
        } else {
            throw new IllegalStateException("invalid file format - " + type);
        }
    }

}
