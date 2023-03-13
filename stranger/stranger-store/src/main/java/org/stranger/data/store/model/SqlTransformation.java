package org.stranger.data.store.model;

import org.stranger.common.model.configuration.Configuration;

public class SqlTransformation extends BaseTransformation {

    private final String queryType;
    private final String value;

    public SqlTransformation(int index, String queryType, String value, View view, Configuration configuration) {
        super(index, view, configuration);
        this.queryType = queryType;
        this.value = value;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getValue() {
        return value;
    }
}
