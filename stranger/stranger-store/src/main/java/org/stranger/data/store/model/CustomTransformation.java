package org.stranger.data.store.model;

import org.stranger.common.model.configuration.Configuration;

public class CustomTransformation extends BaseTransformation {
    private final String implementation;
    public CustomTransformation(int index, String implementation, View view, Configuration configuration) {
        super(index, view, configuration);
        this.implementation = implementation;
    }

    public String getImplementation() {
        return implementation;
    }
}
