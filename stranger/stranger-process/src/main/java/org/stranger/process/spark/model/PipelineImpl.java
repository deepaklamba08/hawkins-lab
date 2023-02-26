package org.stranger.process.spark.model;

import org.stranger.common.model.pipeline.DataSink;
import org.stranger.common.model.pipeline.DataSource;
import org.stranger.common.model.pipeline.Pipeline;
import org.stranger.common.model.pipeline.Transformation;

import java.util.ArrayList;
import java.util.List;

public class PipelineImpl implements Pipeline {

    private final List<DataSource> dataSources;

    private final List<Transformation> transformations;

    private final List<DataSink> dataSinks;

    private PipelineImpl(List<DataSource> dataSources, List<Transformation> transformations, List<DataSink> dataSinks) {
        this.dataSources = dataSources;
        this.transformations = transformations;
        this.dataSinks = dataSinks;
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

    public static class PipelineBuilder {
        private List<DataSource> dataSources;

        private List<Transformation> transformations;

        private List<DataSink> dataSinks;

        public PipelineBuilder withDataSource(DataSource dataSource) {
            if (this.dataSources == null) {
                this.dataSources = new ArrayList<>();
            }
            this.dataSources.add(dataSource);
            return this;
        }

        public PipelineBuilder withTransformation(Transformation transformation) {
            if (this.transformations == null) {
                this.transformations = new ArrayList<>();
            }
            this.transformations.add(transformation);
            return this;
        }

        public PipelineBuilder withDataSink(DataSink dataSink) {
            if (this.dataSinks == null) {
                this.dataSinks = new ArrayList<>();
            }
            this.dataSinks.add(dataSink);
            return this;
        }

        public PipelineBuilder withDataSources(List<DataSource> dataSources) {
            if (this.dataSources == null) {
                this.dataSources = new ArrayList<>();
            }
            this.dataSources.addAll(dataSources);
            return this;
        }

        public PipelineBuilder withTransformations(List<Transformation> transformations) {
            if (this.transformations == null) {
                this.transformations = new ArrayList<>();
            }
            this.transformations.addAll(transformations);
            return this;
        }

        public PipelineBuilder withDataSink(List<DataSink> dataSinks) {
            if (this.dataSinks == null) {
                this.dataSinks = new ArrayList<>();
            }
            this.dataSinks.addAll(dataSinks);
            return this;
        }

        public PipelineImpl build() {
            return new PipelineImpl(this.dataSources, this.transformations, this.dataSinks);
        }
    }

}
