package org.stranger.data.store.repo.test;

import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.id.StringId;
import org.stranger.data.store.repo.ApplicationRepository;
import org.stranger.data.store.repo.impl.JsonApplicationRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

public class TestJsonApplicationRepository {

    private ApplicationRepository appRepository;

    @BeforeSuite
    public void init() {
        appRepository = new JsonApplicationRepository(new File("src/main/resources/repository/json/app_config.json"));
    }

    @Test
    public void testLookupApplication() throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.InvalidConfigurationException {
        Application application = this.appRepository.lookupApplication(new StringId("1"));

        Assert.assertNotNull(application, "application can not be null");
    }
}
