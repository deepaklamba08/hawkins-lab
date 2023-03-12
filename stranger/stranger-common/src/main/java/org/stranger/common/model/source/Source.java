package org.stranger.common.model.source;

import org.stranger.common.model.Entity;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.user.User;

import java.util.Date;

public class Source extends Entity {
    private final SourceDetail sourceDetail;

    protected Source(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, SourceDetail sourceDetail) {
        super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
        this.sourceDetail = sourceDetail;
    }

    public SourceDetail getSourceDetail() {
        return sourceDetail;
    }

    public static class SourceBuilder extends EntityBuilder {

        private SourceDetail sourceDetail;

        public  SourceBuilder(){
            super();
        }
        protected SourceBuilder(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, SourceDetail sourceDetail) {
            super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
            this.sourceDetail = sourceDetail;
        }

        public SourceBuilder withId(Id id) {
            this.id = id;
            return this;
        }
        public SourceBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SourceBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public SourceBuilder withCreateDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public SourceBuilder withUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        public SourceBuilder withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public SourceBuilder withUpdatedBy(User updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public SourceBuilder withActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public SourceBuilder makeActive() {
            this.isActive = true;
            return this;
        }

        public SourceBuilder makeInActive() {
            this.isActive = false;
            return this;
        }

        public SourceBuilder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        public SourceBuilder withSourceDetail(SourceDetail sourceDetail) {
            this.sourceDetail = sourceDetail;
            return this;
        }
        
        @Override
        public Source build() {
            return new Source(this.id, this.name, this.description, this.createDate, this.updateDate, this.createdBy, this.updatedBy, this.isActive, this.configuration, this.sourceDetail);
        }
    }
}
