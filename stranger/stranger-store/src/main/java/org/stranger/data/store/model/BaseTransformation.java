package org.stranger.data.store.model;

import org.stranger.common.model.Entity;
import org.stranger.common.model.application.Transformation;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.user.User;

import java.util.Date;

public abstract class BaseTransformation extends Entity implements Transformation {
    private final int index;
    private final View view;

    public BaseTransformation(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, int index, View view) {
        super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
        this.index = index;
        this.view = view;
    }

    public int getIndex() {
        return index;
    }

    public View getView() {
        return view;
    }


}
