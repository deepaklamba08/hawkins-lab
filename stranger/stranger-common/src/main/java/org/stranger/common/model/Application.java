package org.stranger.common.model;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.user.User;

import java.util.Date;

public class Application extends Entity {

    private final AppType appType;

    protected Application(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, AppType appType) {
        super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
        this.appType = appType;
    }

    public AppType getAppType() {
        return appType;
    }

    public static class ApplicationBuilder extends EntityBuilder {

        private AppType appType;

        public ApplicationBuilder withAppType(AppType appType) {
            this.appType = appType;
            return this;
        }

        protected ApplicationBuilder(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration) {
            super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
        }

        @Override
        public Application build() {
            return new Application(this.id, this.name, this.description, this.createDate, this.updateDate, this.createdBy, this.updatedBy, this.isActive, this.configuration, this.appType);
        }
    }
}
