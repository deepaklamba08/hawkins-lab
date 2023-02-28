package org.stranger.process;

import org.stranger.common.model.application.AppExecutionResult;
import org.stranger.common.model.application.Application;

public interface IApplicationRunner {
    public ExecutionResult execute(Application application);
}
