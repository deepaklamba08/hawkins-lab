package org.stranger.data.store.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.configuration.ConfigurationFactory;
import org.stranger.common.model.configuration.impl.JsonConfiguration;
import org.stranger.common.model.id.Id;
import org.stranger.data.store.repo.ApplicationRepository;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonApplicationRepository implements ApplicationRepository {
    private Logger logger;
    private final File configFile;
    private Map<Id, Application> applications;

    public JsonApplicationRepository(File configFile) {
        this.configFile = configFile;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Application lookupApplication(Id applicationId) throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.InvalidConfigurationException {
        logger.info("Executing : JsonApplicationRepository.lookupApplication(applicationId : {})", applicationId);
        if (applicationId == null) {
            logger.error("applicationId can not be null");
            throw new IllegalArgumentException("applicationId can not be null");
        }
        if (this.applications == null) {
            this.loadApplications();
        }
        Application application = this.applications.get(applicationId);
        if (application == null) {
            throw new StrangerExceptions.ObjectNotFoundException("application not found - " + applicationId);
        }
        return application;
    }

    private void loadApplications() throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.InvalidConfigurationException {
        logger.info("Executing : JsonApplicationRepository.loadApplications()");
        if (this.configFile == null || this.configFile.getAbsolutePath().isEmpty()) {
            logger.error("Config file path can not be null or empty, path - {}", this.configFile);
            throw new IllegalArgumentException("Config file path can not be null or empty, path - " + this.configFile);
        }
        if (!this.configFile.exists()) {
            throw new StrangerExceptions.ObjectNotFoundException("Config file not not found - " + this.configFile.getAbsolutePath());
        }
        JsonConfiguration appConfig = ConfigurationFactory.load(this.configFile, "json");
        if (appConfig.isNull() || !appConfig.isArray()) {
            throw new StrangerExceptions.InvalidConfigurationException("configuration is invalid");
        }
        List<Configuration> appConfigList = appConfig.asList();
        this.applications = new HashMap<>(appConfigList.size());
        for (Configuration appConfigObject : appConfigList) {
            Application application = this.mapApplication(appConfigObject);
            this.applications.put(application.getId(), application);
        }

    }

    private Application mapApplication(Configuration appConfig) throws StrangerExceptions.InvalidConfigurationException {
        if (appConfig.isNull() ) {
            throw new StrangerExceptions.InvalidConfigurationException("configuration is invalid");
        }
        Application.ApplicationBuilder appBuilder =new Application.ApplicationBuilder();


        return appBuilder.build();
    }
}
