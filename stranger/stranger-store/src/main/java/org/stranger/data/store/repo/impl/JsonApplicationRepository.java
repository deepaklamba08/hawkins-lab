package org.stranger.data.store.repo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.AppType;
import org.stranger.common.model.application.Application;
import org.stranger.common.model.application.DataSink;
import org.stranger.common.model.application.DataSource;
import org.stranger.common.model.application.Transformation;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.configuration.ConfigurationFactory;
import org.stranger.common.model.configuration.impl.JsonConfiguration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.id.StringId;
import org.stranger.common.model.source.Source;
import org.stranger.common.model.source.type.FileFormat;
import org.stranger.common.model.source.type.FileSource;
import org.stranger.common.model.trgt.Target;
import org.stranger.common.model.trgt.type.FileTargetDetail;
import org.stranger.common.model.user.User;
import org.stranger.common.util.StrangerConstants;
import org.stranger.data.store.model.DataSinkImpl;
import org.stranger.data.store.model.DataSourceImpl;
import org.stranger.data.store.model.SqlTransformation;
import org.stranger.data.store.model.View;
import org.stranger.data.store.repo.ApplicationRepository;
import scala.App;

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
        JsonConfiguration appConfig = ConfigurationFactory.load(this.configFile, StrangerConstants.REPO_TYPE_JSON);
        if (appConfig.isNull() || !appConfig.isArray()) {
            throw new StrangerExceptions.InvalidConfigurationException("configuration is invalid");
        }
        List<Configuration> appConfigList = appConfig.asList();
        this.applications = new HashMap<>(appConfigList.size());
        for (Configuration appConfigObject : appConfigList) {
            Application application = this.mapApplication(appConfigObject);
            this.applications.put(application.getId(), application);
        }

        logger.info("no of applications loaded - {}",this.applications.size());

    }

    private Application mapApplication(Configuration appConfig) throws StrangerExceptions.InvalidConfigurationException {
        if (appConfig.isNull()) {
            throw new StrangerExceptions.InvalidConfigurationException("configuration is invalid");
        }
        Application.ApplicationBuilder appBuilder = new Application.ApplicationBuilder();

        appBuilder = appBuilder.withId(new StringId(appConfig.getString(StrangerConstants.ID_FIELD)))
                .withName(appConfig.getString(StrangerConstants.NAME_FIELD))
                .withDescription(appConfig.getString(StrangerConstants.DESCRIPTION_FIELD))
                .withCreateDate(appConfig.getDate(StrangerConstants.CREATE_DATE_FIELD))
                .withUpdateDate(appConfig.getDate(StrangerConstants.UPDATE_DATE_FIELD, null))
                .withCreatedBy(new User(appConfig.getString(StrangerConstants.CREATED_BY_FIELD)))
                .withAppType(AppType.of(appConfig.getString(StrangerConstants.TYPE_FIELD)))
                .withActive(appConfig.getBoolean(StrangerConstants.IS_ACTIVE_FIELD, false));

        if (appConfig.hasField(StrangerConstants.UPDATED_BY_FIELD)) {
            appBuilder = appBuilder.withUpdatedBy(new User(appConfig.getString(StrangerConstants.UPDATED_BY_FIELD)));
        }

        if (appConfig.hasField(StrangerConstants.CONFIGURATION_FIELD)) {
            appBuilder = appBuilder.withConfiguration(appConfig.getConfiguration(StrangerConstants.CONFIGURATION_FIELD));
        }

        List<Configuration> sourcesConfig = appConfig.getListValue(StrangerConstants.SOURCES_FIELD);
        for (Configuration sourceConfig : sourcesConfig) {
            appBuilder.withDataSource(this.mapDataSource(sourceConfig));
        }

        List<Configuration> trsConfig = appConfig.getListValue(StrangerConstants.TRANSFORMATIONS_FIELD);
        for (Configuration trConfig : trsConfig) {
            appBuilder.withTransformation(this.mapTransformation(trConfig));
        }

        List<Configuration> sinksConfig = appConfig.getListValue(StrangerConstants.SINKS_FIELD);
        for (Configuration sinkConfig : sinksConfig) {
            appBuilder.withDataSink(this.mapDataSink(sinkConfig));
        }

        return appBuilder.build();
    }

    private DataSink mapDataSink(Configuration sinkConfig) throws StrangerExceptions.InvalidConfigurationException {
        Target.TargetBuilder targetBuilder = new Target.TargetBuilder();

        targetBuilder = targetBuilder.withId(new StringId(sinkConfig.getString(StrangerConstants.ID_FIELD)))
                .withName(sinkConfig.getString(StrangerConstants.NAME_FIELD))
                .withDescription(sinkConfig.getString(StrangerConstants.DESCRIPTION_FIELD))
                .withCreateDate(sinkConfig.getDate(StrangerConstants.CREATE_DATE_FIELD))
                .withUpdateDate(sinkConfig.getDate(StrangerConstants.UPDATE_DATE_FIELD, null))
                .withCreatedBy(new User(sinkConfig.getString(StrangerConstants.CREATED_BY_FIELD)))
                .withActive(sinkConfig.getBoolean(StrangerConstants.IS_ACTIVE_FIELD, false));

        if (sinkConfig.hasField(StrangerConstants.UPDATED_BY_FIELD)) {
            targetBuilder = targetBuilder.withUpdatedBy(new User(sinkConfig.getString(StrangerConstants.UPDATED_BY_FIELD)));
        }

        if (sinkConfig.hasField(StrangerConstants.CONFIGURATION_FIELD)) {
            targetBuilder = targetBuilder.withConfiguration(sinkConfig.getConfiguration(StrangerConstants.CONFIGURATION_FIELD));
        }

        String sinkType = sinkConfig.getString(StrangerConstants.TYPE_FIELD);
        if (StrangerConstants.SINK_TYPE_FILE.equalsIgnoreCase(sinkType)) {
            FileTargetDetail fileTargetDetail = new FileTargetDetail(
                    FileFormat.of(sinkConfig.getString(StrangerConstants.FILE_FORMAT_FIELD)),
                    sinkConfig.getString(StrangerConstants.MODE_FIELD),
                    sinkConfig.getString(StrangerConstants.LOCATION_FIELD),
                    sinkConfig.getConfiguration(StrangerConstants.CONFIGURATION_FIELD, null)
            );

            targetBuilder = targetBuilder.withTargetDetail(fileTargetDetail);
        } else {
            throw new StrangerExceptions.InvalidConfigurationException("invalid sink type - " + sinkType);
        }

        DataSinkImpl dataSink = new DataSinkImpl(sinkConfig.getInt(StrangerConstants.INDEX_FIELD),
                sinkConfig.getString(StrangerConstants.SQL_FIELD),
                targetBuilder.build());
        return dataSink;
    }

    private Transformation mapTransformation(Configuration trConfig) throws StrangerExceptions.InvalidConfigurationException {
        String trType = trConfig.getString(StrangerConstants.TYPE_FIELD);
        if (StrangerConstants.TRANSFORMATION_TYPE_FILE.equalsIgnoreCase(trType)) {
            SqlTransformation sqlTransformation = new SqlTransformation(trConfig.getInt(StrangerConstants.INDEX_FIELD),
                    trConfig.getString(StrangerConstants.QUERY_TYPE_FIELD),
                    trConfig.getString(StrangerConstants.VALUE_FIELD),
                    this.mapView(trConfig.getConfiguration(StrangerConstants.VIEW_FIELD)),
                    trConfig.getConfiguration(StrangerConstants.CONFIGURATION_FIELD, null)
            );

            return sqlTransformation;
        } else {
            throw new StrangerExceptions.InvalidConfigurationException("invalid transformation type - " + trType);
        }
    }

    private DataSource mapDataSource(Configuration sourceConfig) throws StrangerExceptions.InvalidConfigurationException {

        Source.SourceBuilder sourceBuilder = new Source.SourceBuilder();
        sourceBuilder = sourceBuilder.withId(new StringId(sourceConfig.getString(StrangerConstants.ID_FIELD)))
                .withName(sourceConfig.getString(StrangerConstants.NAME_FIELD))
                .withDescription(sourceConfig.getString(StrangerConstants.DESCRIPTION_FIELD))
                .withCreateDate(sourceConfig.getDate(StrangerConstants.CREATE_DATE_FIELD))
                .withUpdateDate(sourceConfig.getDate(StrangerConstants.UPDATE_DATE_FIELD, null))
                .withCreatedBy(new User(sourceConfig.getString(StrangerConstants.CREATED_BY_FIELD)))
                .withActive(sourceConfig.getBoolean(StrangerConstants.IS_ACTIVE_FIELD, false));

        if (sourceConfig.hasField(StrangerConstants.UPDATED_BY_FIELD)) {
            sourceBuilder = sourceBuilder.withUpdatedBy(new User(sourceConfig.getString(StrangerConstants.UPDATED_BY_FIELD)));
        }

        if (sourceConfig.hasField(StrangerConstants.CONFIGURATION_FIELD)) {
            sourceBuilder = sourceBuilder.withConfiguration(sourceConfig.getConfiguration(StrangerConstants.CONFIGURATION_FIELD));
        }

        String sourceType = sourceConfig.getString(StrangerConstants.TYPE_FIELD);
        if (StrangerConstants.SOURCE_TYPE_FILE.equalsIgnoreCase(sourceType)) {
            FileSource fileSource = new FileSource(
                    FileFormat.of(sourceConfig.getString(StrangerConstants.FILE_FORMAT_FIELD)),
                    sourceConfig.getString(StrangerConstants.LOCATION_FIELD),
                    sourceConfig.getConfiguration(StrangerConstants.CONFIGURATION_FIELD, null)
            );
            sourceBuilder = sourceBuilder.withSourceDetail(fileSource);
        } else {
            throw new StrangerExceptions.InvalidConfigurationException("invalid source type - " + sourceType);
        }
        DataSourceImpl dataSource = new DataSourceImpl(sourceConfig.getInt(StrangerConstants.INDEX_FIELD),
                this.mapView(sourceConfig.getConfiguration(StrangerConstants.VIEW_FIELD)),
                sourceBuilder.build()
        );

        return dataSource;
    }

    private View mapView(Configuration viewConfig) {
        return new View(viewConfig.getString(StrangerConstants.NAME_FIELD), StrangerConstants.VIEW_TYPE_GLOBAL_TEMP,
                viewConfig.getBoolean(StrangerConstants.IS_PERSIST_FIELD, false),
                viewConfig.getString(StrangerConstants.PERSIST_MODE_FIELD, StrangerConstants.PERSIST_MODE_MEMORY_AND_DISK));
    }
}
