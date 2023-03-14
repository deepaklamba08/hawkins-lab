package org.stranger.data.store.model;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.user.User;

import java.util.Date;

public class SqlTransformation extends BaseTransformation {

    private final String queryType;
    private final String value;

    public SqlTransformation(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, int index, View view, String queryType, String value) {
        super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration, index, view);
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
