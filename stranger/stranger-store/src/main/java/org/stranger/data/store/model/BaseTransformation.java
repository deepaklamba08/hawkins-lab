package org.stranger.data.store.model;

import org.stranger.common.model.application.Transformation;
import org.stranger.common.model.configuration.Configuration;

public abstract class BaseTransformation implements Transformation {
    private final int index;
    private final View view;
    private final Configuration configuration;

    public BaseTransformation(int index, View view, Configuration configuration) {
        this.index = index;
        this.view = view;
        this.configuration = configuration;
    }

    public int getIndex() {
        return index;
    }

    public View getView() {
        return view;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
