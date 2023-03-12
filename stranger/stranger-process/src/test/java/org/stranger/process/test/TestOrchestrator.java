package org.stranger.process.test;

import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.id.StringId;
import org.stranger.data.store.repo.ApplicationRepository;
import org.stranger.data.store.repo.ExecutionResultRepository;
import org.stranger.data.store.repo.impl.JsonApplicationRepository;
import org.stranger.process.IApplicationRunner;
import org.stranger.process.Orchestrator;
import org.stranger.process.spark.engine.SparkApplicationRunner;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.Properties;

public class TestOrchestrator {

    private Orchestrator orchestrator;

    @BeforeSuite
    public void init() {
        URL res = this.getClass().getClassLoader().getResource("stranger/stranger-store/src/main/resources/repository/json/app_config.json");
        ApplicationRepository applicationRepository = new JsonApplicationRepository(new File(""));
        ExecutionResultRepository executionResultRepository = null;

        Properties executionProperties = null;
        IApplicationRunner applicationRunner = new SparkApplicationRunner(executionProperties);
        this.orchestrator = new Orchestrator(applicationRepository, executionResultRepository, applicationRunner);
    }
    
    @Test
    public void testExecuteApplication() throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.InvalidStateException, StrangerExceptions.InvalidConfigurationException {
        this.orchestrator.executeApplication(new StringId("1"),"xyz");
    }
}
