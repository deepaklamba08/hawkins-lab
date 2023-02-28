package org.stranger.data.store.model;

import org.stranger.common.model.application.DataSink;
import org.stranger.common.model.trgt.Target;

public class DataSinkImpl implements DataSink {
    private final int index;
    private final Target target;

    public DataSinkImpl(int index, Target target) {
        this.index = index;
        this.target = target;
    }
}
