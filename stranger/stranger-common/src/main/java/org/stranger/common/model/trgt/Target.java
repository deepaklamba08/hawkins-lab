package org.stranger.common.model.trgt;

import org.stranger.common.model.Entity;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.user.User;

import java.util.Date;

public class Target extends Entity {
    private final TargetDetail targetDetail;

    protected Target(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, TargetDetail targetDetail) {
        super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
        this.targetDetail = targetDetail;
    }

    public TargetDetail getTargetDetail() {
        return targetDetail;
    }

    public static class TargetBuilder extends EntityBuilder {

        private TargetDetail targetDetail;


        public TargetBuilder(){
            super();
        }
        protected TargetBuilder(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, TargetDetail targetDetail) {
            super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
            this.targetDetail = targetDetail;
        }
        public TargetBuilder withId(Id id) {
            this.id = id;
            return this;
        }
        public TargetBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public TargetBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TargetBuilder withCreateDate(Date createDate) {
            this.createDate = createDate;
            return this;
        }

        public TargetBuilder withUpdateDate(Date updateDate) {
            this.updateDate = updateDate;
            return this;
        }

        public TargetBuilder withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public TargetBuilder withUpdatedBy(User updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public TargetBuilder withActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TargetBuilder makeActive() {
            this.isActive = true;
            return this;
        }

        public TargetBuilder makeInActive() {
            this.isActive = false;
            return this;
        }

        public TargetBuilder withConfiguration(Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        public TargetBuilder withTargetDetail(TargetDetail targetDetail) {
            this.targetDetail = targetDetail;
            return this;
        }

        @Override
        public Target build() {
            return new Target(this.id, this.name, this.description, this.createDate, this.updateDate, this.createdBy, this.updatedBy, this.isActive, this.configuration, this.targetDetail);
        }
    }
}