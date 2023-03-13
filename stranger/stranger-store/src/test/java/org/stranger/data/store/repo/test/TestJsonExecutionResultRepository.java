package org.stranger.data.store.repo.test;

import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.AppExecutionResult;
import org.stranger.common.model.id.StringId;
import org.stranger.data.store.repo.impl.JsonExecutionResultRepository;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class TestJsonExecutionResultRepository {
    private JsonExecutionResultRepository executionResultRepository;

    @BeforeSuite
    public void init() {
        this.executionResultRepository = new JsonExecutionResultRepository(new File("target/execution"));
    }

    @Test
    public void testSave() throws StrangerExceptions.SystemFailureException {
        this.executionResultRepository.storeResult(new StringId("1"), AppExecutionResult.AppExecutionStatus.RUNNING, "sunning", "xyz", new Date());
    }

}
