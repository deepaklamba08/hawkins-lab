package org.stranger.data.store.model;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.user.User;

import java.util.Date;

public class CustomTransformation extends BaseTransformation {
    private final String implementation;

    public CustomTransformation(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, int index, View view, String implementation) {
        super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration, index, view);
        this.implementation = implementation;
    }

    public String getImplementation() {
        return implementation;
    }
}
