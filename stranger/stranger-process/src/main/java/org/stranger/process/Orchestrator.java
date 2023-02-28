package org.stranger.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.AppExecutionResult;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.id.Id;
import org.stranger.common.util.StrangerConstants;
import org.stranger.data.store.repo.ApplicationRepository;
import org.stranger.data.store.repo.ExecutionResultRepository;

import java.util.Date;

public class Orchestrator {

    private Logger logger;
    private ApplicationRepository applicationRepository;
    private ExecutionResultRepository executionResultRepository;
    private IApplicationRunner applicationRunner;

    public Orchestrator(ApplicationRepository applicationRepository, ExecutionResultRepository executionResultRepository, IApplicationRunner applicationRunner) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.applicationRepository = applicationRepository;
        this.executionResultRepository = executionResultRepository;
        this.applicationRunner = applicationRunner;
    }

    public void executeApplication(Id applicationId, String submitter) throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.InvalidStateException {
        logger.info("Executing : Orchestrator.executeApplication(applicationId : {}, submitter: {})", applicationId, submitter);
        if (applicationId == null || submitter == null || submitter.isEmpty()) {
            logger.error("Invalid arguments, applicationId : {}, submitter : {}", applicationId, submitter);
        }

        logger.debug("loading application from repository....");
        Application application = this.applicationRepository.lookupApplication(applicationId);
        logger.debug("application loaded for id : {}, name : {}", applicationId, application.getName());

        if (!application.isActive()) {
            logger.error("Application - {} is inactive", application.getName());
            throw new StrangerExceptions.InvalidStateException("Application - " + application.getName() + " is inactive");
        }

        Id executionId = this.executionResultRepository.storeResult(AppExecutionResult.AppExecutionStatus.RUNNING, StrangerConstants.APP_EXE_RUNNING_MESSAGE, submitter, new Date());
        logger.debug("application execution created with id - {}", executionId);

        logger.debug("calling application runner ...");
        ExecutionResult executionResult = this.applicationRunner.execute(application);
        logger.info("application runner execution completed with status - {}", executionResult.getExecutionStatus());

        AppExecutionResult.AppExecutionStatus executionStatus = this.mapAppExecutionStatus(executionResult.getExecutionStatus());
        this.executionResultRepository.updateResult(executionId, executionStatus, executionResult.getExecutionMessage(), new Date(), executionResult.getMetrics());
        logger.info("Exiting : Orchestrator.executeApplication()");
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
