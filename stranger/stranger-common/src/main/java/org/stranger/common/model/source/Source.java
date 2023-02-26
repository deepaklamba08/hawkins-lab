package org.stranger.common.model.source;

import org.stranger.common.model.Entity;
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

        public SourceBuilder withSourceDetail(SourceDetail sourceDetail) {
            this.sourceDetail = sourceDetail;
            return this;
        }

        protected SourceBuilder(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, SourceDetail sourceDetail) {
            super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
            this.sourceDetail = sourceDetail;
        }

        @Override
        public Source build() {
            return new Source(this.id, this.name, this.description, this.createDate, this.updateDate, this.createdBy, this.updatedBy, this.isActive, this.configuration, this.sourceDetail);
        }
    }
}
