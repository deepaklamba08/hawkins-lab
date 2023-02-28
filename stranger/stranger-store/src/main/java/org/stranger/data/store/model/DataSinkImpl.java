package org.stranger.data.store.model;

import org.stranger.common.model.application.DataSink;
import org.stranger.common.model.trgt.Target;

public class DataSinkImpl implements DataSink {
    private final int index;

    private final String sql;
    private final Target target;


    public DataSinkImpl(int index, String sql, Target target) {
        this.index = index;
        this.sql = sql;
        this.target = target;
    }

    public int getIndex() {
        return index;
    }

    public Target getTarget() {
        return target;
    }

    public String getSql() {
        return sql;
    }
}
