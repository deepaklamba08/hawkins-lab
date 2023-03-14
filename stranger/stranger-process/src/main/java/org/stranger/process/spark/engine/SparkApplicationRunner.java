package org.stranger.process.spark.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stranger.common.model.application.Application;
import org.stranger.common.rc.RuntimeConfiguration;
import org.stranger.process.ExecutionResult;
import org.stranger.process.IApplicationRunner;
import org.stranger.process.spark.execution.AppProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SparkApplicationRunner implements IApplicationRunner {
    private Logger logger;
    private RuntimeConfiguration runtimeConfiguration;

    public SparkApplicationRunner(RuntimeConfiguration runtimeConfiguration) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.runtimeConfiguration = runtimeConfiguration;
    }

    @Override
    public ExecutionResult execute(Application application) {
        logger.info("Executing : SparkApplicationRunner.execute(applicationId : {})", application.getId());
        if (application == null) {
            logger.error("Invalid arguments, application is null");
            throw new IllegalStateException("Invalid arguments, application is null");
        }
        ExecutionResult result = AppProcessor.apply(runtimeConfiguration).process(application);

        logger.info("Exiting : SparkApplicationRunner.execute()");
        return result;

    }

    /*private Map<String, String> toExecutionProperties() {
        Set<Object> keys = this.executionProperties.keySet();
        Map<String, String> props = new HashMap<>(keys.size());
        for (Object key : keys) {
            props.put(key.toString(), this.executionProperties.getProperty(key.toString()));
        }
        return props;
    }*/
}
