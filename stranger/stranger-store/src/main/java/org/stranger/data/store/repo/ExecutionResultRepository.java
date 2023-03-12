package org.stranger.data.store.repo;

import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.AppExecutionResult;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.id.Id;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ExecutionResultRepository {
    public Id storeResult(Id appId,AppExecutionResult.AppExecutionStatus executionStatus, String message, String runBy, Date startDate);

    public Id updateResult(Id executionId, AppExecutionResult.AppExecutionStatus executionStatus, String message, Date endDate, Configuration metrics) throws StrangerExceptions.ObjectNotFoundException;

    public AppExecutionResult lookupResult(Id executionId) throws StrangerExceptions.ObjectNotFoundException;

    public List<AppExecutionResult> lookupResults(Id applicationId);

    public List<AppExecutionResult> lookupResults(Id applicationId, Set<AppExecutionResult.AppExecutionStatus> statuses);

}
