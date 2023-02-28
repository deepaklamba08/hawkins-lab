package org.stranger.process.spark.engine;

import org.slf4j.Logger;
import org.stranger.common.model.application.Application;
import org.stranger.process.ExecutionResult;
import org.stranger.process.IApplicationRunner;
import org.stranger.process.spark.execution.AppProcessor;

public class SparkApplicationRunner implements IApplicationRunner {
    private Logger logger;

    @Override
    public ExecutionResult execute(Application application) {
        logger.info("Executing : SparkApplicationRunner.execute()");
        if (application == null) {
            logger.error("Invalid arguments, application is null");
            throw new IllegalStateException("Invalid arguments, application is null");
        }
        ExecutionResult result = AppProcessor.process(application);

        logger.info("Exiting : SparkApplicationRunner.execute()");
        return result;

    }
}
