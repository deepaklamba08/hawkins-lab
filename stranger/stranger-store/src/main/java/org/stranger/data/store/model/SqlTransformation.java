package org.stranger.data.store.model;

import org.stranger.common.model.application.Transformation;

public class SqlTransformation implements Transformation {
    private final int index;
    private final String queryType;
    private final String value;

    private final View view;

    public SqlTransformation(int index, String queryType, String value, View view) {
        this.index = index;
        this.queryType = queryType;
        this.value = value;
        this.view = view;
    }

    public int getIndex() {
        return index;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getValue() {
        return value;
    }

    public View getView() {
        return view;
    }
}
