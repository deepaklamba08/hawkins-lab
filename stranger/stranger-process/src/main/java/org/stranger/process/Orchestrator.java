package org.stranger.process;

import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.AppExecutionResult;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.id.Id;
import org.stranger.data.store.repo.ApplicationRepository;
import org.stranger.data.store.repo.ExecutionResultRepository;

import java.util.Date;

public class Orchestrator {

    private final String APP_EXE_MESSAGE = "";
    private ApplicationRepository applicationRepository;
    private ExecutionResultRepository executionResultRepository;

    private IApplicationRunner applicationRunner;

    public void executeApplication(Id applicationId, String submitter) throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.InvalidStateException {

        Application application = this.applicationRepository.lookupApplication(applicationId);

        if (!application.isActive()) {
            throw new StrangerExceptions.InvalidStateException("Application - " + application.getName() + " is inactive");
        }

        Id executionId = this.executionResultRepository.storeResult(AppExecutionResult.AppExecutionStatus.RUNNING, APP_EXE_MESSAGE, submitter, new Date());

        ExecutionResult executionResult = this.applicationRunner.execute(application);
        AppExecutionResult.AppExecutionStatus executionStatus = this.mapAppExecutionStatus(executionResult.getExecutionStatus());
        this.executionResultRepository.updateResult(executionId, executionStatus, executionResult.getExecutionMessage(), new Date(), executionResult.getMetrics())

    }

    private AppExecutionResult.AppExecutionStatus mapAppExecutionStatus(ExecutionResult.ExecutionStatus executionStatus) {
        if (executionStatus == ExecutionResult.ExecutionStatus.SUCCESS) {
            return AppExecutionResult.AppExecutionStatus.SUCCESS;
        } else if (executionStatus == ExecutionResult.ExecutionStatus.FAILED) {
            return AppExecutionResult.AppExecutionStatus.FAILED;
        }
        return AppExecutionResult.AppExecutionStatus.FAILED;
    }
}
