package org.stranger.data.store.model;

import org.stranger.common.model.application.Transformation;

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
