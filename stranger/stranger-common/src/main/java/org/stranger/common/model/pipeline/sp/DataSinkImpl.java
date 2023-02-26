package org.stranger.common.model.pipeline.sp;

import org.stranger.common.trgt.Target;

public class DataSinkImpl {
    private final int index;
    private final Target target;

    public DataSinkImpl(int index, Target target) {
        this.index = index;
        this.target = target;
    }
}
