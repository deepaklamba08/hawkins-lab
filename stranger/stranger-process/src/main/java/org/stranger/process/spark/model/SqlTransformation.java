package org.stranger.process.spark.model;

import org.stranger.common.model.pipeline.Transformation;

public class SqlTransformation implements Transformation {
    private final int index;
    private final String queryType;
    private final String value;

    public SqlTransformation(int index, String queryType, String value) {
        this.index = index;
        this.queryType = queryType;
        this.value = value;
    }
}
