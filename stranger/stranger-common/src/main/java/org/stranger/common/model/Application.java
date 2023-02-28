package org.stranger.common.model;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.pipeline.DataSink;
import org.stranger.common.model.pipeline.DataSource;
import org.stranger.common.model.pipeline.Transformation;
import org.stranger.common.model.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application extends Entity {

    private final AppType appType;
    private final List<DataSource> dataSources;

    private final List<Transformation> transformations;

    private final List<DataSink> dataSinks;
    protected Application(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration, AppType appType, List<DataSource> dataSources, List<Transformation> transformations, List<DataSink> dataSinks) {
        super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
        this.appType = appType;
        this.dataSources = dataSources;
        this.transformations = transformations;
        this.dataSinks = dataSinks;
    }

    public AppType getAppType() {
        return appType;
    }

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public List<Transformation> getTransformations() {
        return transformations;
    }

    public List<DataSink> getDataSinks() {
        return dataSinks;
    }

    public static class ApplicationBuilder extends EntityBuilder {

        private AppType appType;
        private List<DataSource> dataSources;

        private List<Transformation> transformations;

        private List<DataSink> dataSinks;
        protected ApplicationBuilder(Id id, String name, String description, Date createDate, Date updateDate, User createdBy, User updatedBy, boolean isActive, Configuration configuration) {
            super(id, name, description, createDate, updateDate, createdBy, updatedBy, isActive, configuration);
        }
        public ApplicationBuilder withAppType(AppType appType) {
            this.appType = appType;
            return this;
        }

        public ApplicationBuilder withDataSource(DataSource dataSource) {
            if (this.dataSources == null) {
                this.dataSources = new ArrayList<>();
            }
            this.dataSources.add(dataSource);
            return this;
        }

        public ApplicationBuilder withTransformation(Transformation transformation) {
            if (this.transformations == null) {
                this.transformations = new ArrayList<>();
            }
            this.transformations.add(transformation);
            return this;
        }

        public ApplicationBuilder withDataSink(DataSink dataSink) {
            if (this.dataSinks == null) {
                this.dataSinks = new ArrayList<>();
            }
            this.dataSinks.add(dataSink);
            return this;
        }

        public ApplicationBuilder withDataSources(List<DataSource> dataSources) {
            if (this.dataSources == null) {
                this.dataSources = new ArrayList<>();
            }
            this.dataSources.addAll(dataSources);
            return this;
        }

        public ApplicationBuilder withTransformations(List<Transformation> transformations) {
            if (this.transformations == null) {
                this.transformations = new ArrayList<>();
            }
            this.transformations.addAll(transformations);
            return this;
        }

        public ApplicationBuilder withDataSink(List<DataSink> dataSinks) {
            if (this.dataSinks == null) {
                this.dataSinks = new ArrayList<>();
            }
            this.dataSinks.addAll(dataSinks);
            return this;
        }
        @Override
        public Application build() {
            return new Application(this.id, this.name, this.description, this.createDate, this.updateDate, this.createdBy, this.updatedBy, this.isActive, this.configuration, this.appType, this.dataSources, this.transformations, this.dataSinks);
        }
    }
}
