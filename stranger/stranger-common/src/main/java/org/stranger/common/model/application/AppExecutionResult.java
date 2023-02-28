package org.stranger.common.model.application;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;

import java.util.Date;

public class AppExecutionResult {

    public enum AppExecutionStatus {
        RUNNING, SUCCESS, FAILED
    }

    private final Id runId;
    private final String engine;
    private final String runBy;

    private final Date startDate;

    private final Date endDate;

    private final AppExecutionStatus appExecutionStatus;

    private final String executionMessage;

    private final Configuration metrics;

    public AppExecutionResult(Id runId, String engine, String runBy, Date startDate, Date endDate, AppExecutionStatus appExecutionStatus, String executionMessage, Configuration metrics) {
        this.runId = runId;
        this.engine = engine;
        this.runBy = runBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.appExecutionStatus = appExecutionStatus;
        this.executionMessage = executionMessage;
        this.metrics = metrics;
    }

    public static class AppExecutionResultBuilder {
        private Id runId;
        private String engine;
        private String runBy;

        private Date startDate;

        private Date endDate;

        private AppExecutionStatus appExecutionStatus;

        private String executionMessage;

        private Configuration metrics;

        public AppExecutionResultBuilder(){}

        public AppExecutionResultBuilder withRunId(Id runId) {
            this.runId = runId;
            return this;
        }

        public AppExecutionResultBuilder withEngine(String engine) {
            this.engine = engine;
            return this;
        }

        public AppExecutionResultBuilder withRunBy(String runBy) {
            this.runBy = runBy;
            return this;
        }

        public AppExecutionResultBuilder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public AppExecutionResultBuilder withEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public AppExecutionResultBuilder withAppExecutionStatus(AppExecutionStatus appExecutionStatus) {
            this.appExecutionStatus = appExecutionStatus;
            return this;
        }

        public AppExecutionResultBuilder withExecutionMessage(String executionMessage) {
            this.executionMessage = executionMessage;
            return this;
        }

        public AppExecutionResultBuilder withMetrics(Configuration metrics) {
            this.metrics = metrics;
            return this;
        }

        public AppExecutionResult build() {
            return new AppExecutionResult(this.runId, this.engine, this.runBy, this.startDate, this.endDate, this.appExecutionStatus, this.executionMessage, this.metrics);
        }

    }
}
