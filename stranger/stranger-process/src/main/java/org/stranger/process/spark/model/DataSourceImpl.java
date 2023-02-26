package org.stranger.process.spark.model;

import org.stranger.common.model.source.Source;

public class DataSourceImpl {
    private final int index;
    private final View view;
    private final Source source;

    public DataSourceImpl(int index, View view, Source source) {
        this.index = index;
        this.view = view;
        this.source = source;
    }
}
