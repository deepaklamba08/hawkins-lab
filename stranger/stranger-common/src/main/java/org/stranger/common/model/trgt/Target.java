package org.stranger.common.model.trgt;

import org.stranger.common.model.Entity;
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

        public TargetBuilder withTargetDetail(TargetDetail targetDetail) {
            this.targetDetail = targetDetail;
            return this;
        }

        protected TargetBuilder(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, TargetDetail targetDetail) {
            super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
            this.targetDetail = targetDetail;
        }

        @Override
        public Target build() {
            return new Target(this.id, this.name, this.description, this.createDate, this.updateDate, this.createdBy, this.updatedBy, this.isActive, this.configuration, this.targetDetail);
        }
    }
}