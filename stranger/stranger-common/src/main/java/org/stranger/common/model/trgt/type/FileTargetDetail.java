package org.stranger.common.model.trgt.type;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.source.type.FileFormat;
import org.stranger.common.model.trgt.TargetDetail;

public class FileTargetDetail implements TargetDetail {
    private final FileFormat fileFormat;
    private final String mode;

    private final String location;
    private final Configuration configuration;

    public FileTargetDetail(FileFormat fileFormat, String mode, String location, Configuration configuration) {
        this.fileFormat = fileFormat;
        this.mode = mode;
        this.location = location;
        this.configuration = configuration;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public String getMode() {
        return mode;
    }

    public String getLocation() {
        return location;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
