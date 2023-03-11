package org.stranger.common.model;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.user.User;

import java.util.Date;

public abstract class Entity {

    private final Id id;
    private final String name;
    private final String description;
    private final Date createDate;
    private final Date updateDate;

    private final User createdBy;
    private final User updatedBy;

    private final boolean isActive;

    private final Configuration configuration;

    protected Entity(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.isActive = isActive;
        this.configuration = configuration;
    }

    public Id getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public boolean isActive() {
        return isActive;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static abstract class EntityBuilder {

        protected Id id;
        protected String name;
        protected String description;
        protected Date createDate;
        protected Date updateDate;
        protected User createdBy;
        protected User updatedBy;
        protected boolean isActive;
        protected Configuration configuration;

        protected EntityBuilder(){}

        protected EntityBuilder(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.createDate = createDate;
            this.updateDate = updateDate;
            this.createdBy = createdBy;
            this.updatedBy = updatedBy;
            this.isActive = isActive;
            this.configuration = configuration;
        }

        public EntityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EntityBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EntityBuilder withCreateDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public EntityBuilder withUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        public EntityBuilder withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public EntityBuilder withUpdatedBy(User updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public EntityBuilder withActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public EntityBuilder makeActive() {
            this.isActive = true;
            return this;
        }

        public EntityBuilder makeInActive() {
            this.isActive = false;
            return this;
        }

        public EntityBuilder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public abstract Entity build();
    }
}
