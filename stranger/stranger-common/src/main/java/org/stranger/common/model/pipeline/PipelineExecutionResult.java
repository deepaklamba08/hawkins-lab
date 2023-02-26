package org.stranger.common.model.pipeline;

import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;

import java.util.Date;

public class PipelineExecutionResult {

    public enum ExecutionStatus {

    }

    private final Id runId;
    private final String engine;
    private final String runBy;

    private final Date startDate;

    private final Date endDate;

    private final ExecutionStatus executionStatus;

    private final String executionMessage;

    private final Configuration metrics;

    public PipelineExecutionResult(Id runId, String engine, String runBy, Date startDate, Date endDate, ExecutionStatus executionStatus, String executionMessage, Configuration metrics) {
        this.runId = runId;
        this.engine = engine;
        this.runBy = runBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.executionStatus = executionStatus;
        this.executionMessage = executionMessage;
        this.metrics = metrics;
    }
}
