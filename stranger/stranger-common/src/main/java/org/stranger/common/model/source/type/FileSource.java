package org.stranger.common.model.source.type;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.source.SourceDetail;

public class FileSource implements SourceDetail {
    private final FileFormat fileFormat;
    private final String location;

    private final Configuration configuration;

    public FileSource(FileFormat fileFormat, String location, Configuration configuration) {
        this.fileFormat = fileFormat;
        this.location = location;
        this.configuration = configuration;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public String getLocation() {
        return location;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
