package org.stranger.common.model.source.type;


public enum FileFormat {
    CSV, PARQUET, ORC, AVRO, JSON;

    public static FileFormat of(String type) {
        if (CSV.name().equalsIgnoreCase(type)) {
            return FileFormat.CSV;
        } else if (PARQUET.name().equalsIgnoreCase(type)) {
            return FileFormat.PARQUET;
        } else if (ORC.name().equalsIgnoreCase(type)) {
            return FileFormat.ORC;
        } else if (AVRO.name().equalsIgnoreCase(type)) {
            return FileFormat.AVRO;
        } else if (JSON.name().equalsIgnoreCase(type)) {
            return FileFormat.JSON;
        } else {
            throw new IllegalStateException("invalid file format - " + type);
        }
    }

}
