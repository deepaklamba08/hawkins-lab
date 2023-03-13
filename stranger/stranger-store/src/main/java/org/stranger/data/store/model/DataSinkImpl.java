package org.stranger.data.store.model;

import org.stranger.common.model.application.DataSink;
import org.stranger.common.model.trgt.Target;

public class DataSinkImpl implements DataSink {
    private final int index;
    private final String queryType;
    private final String value;
    private final Target target;


    public DataSinkImpl(int index, String queryType, String value, Target target) {
        this.index = index;
        this.queryType = queryType;
        this.value = value;
        this.target = target;
    }

    public int getIndex() {
        return index;
    }

    public Target getTarget() {
        return target;
    }

    public String getQueryType() {
        return queryType;
    }

    public String getValue() {
        return value;
    }
}
