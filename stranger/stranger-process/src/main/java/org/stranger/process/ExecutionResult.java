package org.stranger.process;

import org.stranger.common.model.configuration.Configuration;

public class ExecutionResult {

    public enum ExecutionStatus {
        SUCCESS, FAILED
    }

    private final ExecutionStatus executionStatus;
    private final String executionMessage;
    private final Configuration metrics;

    public ExecutionResult(ExecutionStatus executionStatus, String executionMessage, Configuration metrics) {
        this.executionStatus = executionStatus;
        this.executionMessage = executionMessage;
        this.metrics = metrics;
    }

    public ExecutionStatus getExecutionStatus() {
        return executionStatus;
    }

    public String getExecutionMessage() {
        return executionMessage;
    }

    public Configuration getMetrics() {
        return metrics;
    }
}
