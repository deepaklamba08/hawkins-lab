package org.stranger.process.spark.model;

import org.stranger.common.model.trgt.Target;

public class DataSinkImpl {
    private final int index;
    private final Target target;

    public DataSinkImpl(int index, Target target) {
        this.index = index;
        this.target = target;
    }
}
