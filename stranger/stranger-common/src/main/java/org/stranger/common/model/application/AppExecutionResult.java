package org.stranger.common.model.application;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;

import java.util.Date;

public class AppExecutionResult {

    public enum AppExecutionStatus {
        RUNNING, SUCCESS, FAILED;

        public AppExecutionStatus getStatus(String status) {
            return null;
        }
    }

    private final Id runId;
    private final Id appId;
    private final String engine;
    private final String runBy;

    private final Date startDate;

    private final Date endDate;

    private final AppExecutionStatus appExecutionStatus;

    private final String executionMessage;

    private final Configuration metrics;

    public AppExecutionResult(Id runId, Id appId, String engine, String runBy, Date startDate, Date endDate, AppExecutionStatus appExecutionStatus, String executionMessage, Configuration metrics) {
        this.runId = runId;
        this.appId = appId;
        this.engine = engine;
        this.runBy = runBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.appExecutionStatus = appExecutionStatus;
        this.executionMessage = executionMessage;
        this.metrics = metrics;
    }

    public Id getRunId() {
        return runId;
    }

    public Id getAppId() {
        return appId;
    }

    public String getEngine() {
        return engine;
    }

    public String getRunBy() {
        return runBy;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public AppExecutionStatus getAppExecutionStatus() {
        return appExecutionStatus;
    }

    public String getExecutionMessage() {
        return executionMessage;
    }

    public Configuration getMetrics() {
        return metrics;
    }

    public static class AppExecutionResultBuilder {
        private Id runId;
        private Id appId;
        private String engine;
        private String runBy;

        private Date startDate;

        private Date endDate;

        private AppExecutionStatus appExecutionStatus;

        private String executionMessage;

        private Configuration metrics;

        public AppExecutionResultBuilder() {
        }

        public AppExecutionResultBuilder withRunId(Id runId) {
            this.runId = runId;
            return this;
        } public AppExecutionResultBuilder withAppId(Id appId) {
            this.appId = appId;
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
            return new AppExecutionResult(this.runId, this.appId, this.engine, this.runBy, this.startDate, this.endDate, this.appExecutionStatus, this.executionMessage, this.metrics);
        }

    }
}
