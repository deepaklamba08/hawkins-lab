package org.stranger.data.store.repo.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.AppExecutionResult;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.configuration.ConfigurationFactory;
import org.stranger.common.model.configuration.impl.JsonConfiguration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.id.StringId;
import org.stranger.common.util.StrangerConstants;
import org.stranger.data.store.repo.ExecutionResultRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JsonExecutionResultRepository implements ExecutionResultRepository {

    private final String FILE_NAME = "summary.json";
    private Logger logger;
    private final File summaryDirectory;
    private File summaryFile;

    private final ObjectWriter summaryWriter = ConfigurationFactory.getObjectMapper().writerFor(new TypeReference<List<SummaryDTO>>() {
    });
    private final ObjectReader summaryReader = ConfigurationFactory.getObjectMapper().readerFor(new TypeReference<List<SummaryDTO>>() {
    });

    public JsonExecutionResultRepository(File summaryDirectory) {
        this.summaryDirectory = summaryDirectory;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public Id storeResult(Id appId, AppExecutionResult.AppExecutionStatus executionStatus, String message, String runBy, Date startDate) throws StrangerExceptions.SystemFailureException {
        logger.info("Executing : JsonExecutionResultRepository.storeResult(appId : {},executionStatus : {}, message : {}, runBy : {}, startDate : {})", appId, executionStatus, message, runBy, startDate);
        if (appId == null || executionStatus == null || message == null || message.isEmpty() || runBy == null || runBy.isEmpty() || startDate == null) {
            logger.error("invalid arguments, appId : {},executionStatus : {}, message : {}, runBy : {}, startDate : {}", appId, executionStatus, message, runBy, startDate);
            throw new IllegalArgumentException("invalid arguments, appId : " + appId + ",executionStatus : " + executionStatus + ", message : " + message + ", runBy : " + runBy + ", startDate : " + startDate);
        }

        List<SummaryDTO> allResults = this.load();
        Id id = this.randomId();
        SummaryDTO summaryDTO = new SummaryDTO(id.getValue(), appId.getValue(), StrangerConstants.EXECUTION_ENGINE_SPARK, runBy, startDate, executionStatus.name(), message);
        allResults.add(summaryDTO);
        this.writeResult(allResults);
        logger.info("Exiting : JsonExecutionResultRepository.storeResult()");
        return id;
    }

    @Override
    public Id updateResult(Id executionId, AppExecutionResult.AppExecutionStatus executionStatus, String message, Date endDate, Configuration metrics) throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.SystemFailureException {
        logger.info("Executing : JsonExecutionResultRepository.updateResult(executionId : {},executionStatus : {}, message : {}, endDate : {}, metrics : {})", executionId, executionStatus, message, endDate, metrics);
        if (executionId == null || executionStatus == null || message == null || message.isEmpty() || endDate == null) {
            logger.error("invalid arguments, executionId : {},executionStatus : {}, message : {}, endDate : {}", executionId, executionStatus, message, endDate);
            throw new IllegalArgumentException("invalid arguments, executionId : " + executionId + ",executionStatus : " + executionStatus + ", message : " + message + ", endDate : " + endDate);
        }
        List<SummaryDTO> allResults = this.load();
        Optional<SummaryDTO> summaryOption = allResults.stream().filter(dto -> dto.getRunId().equals(executionId.getValue())).findAny();
        if (!summaryOption.isPresent()) {
            logger.error("summary not found for id - {}", executionId);
            throw new StrangerExceptions.ObjectNotFoundException("summary not found for id - " + executionId);
        }
        SummaryDTO summary = summaryOption.get();
        summary.setAppExecutionStatus(executionStatus.name());
        summary.setExecutionMessage(message);
        summary.setEndDate(endDate);

        if (metrics != null) {
            summary.setMetrics(this.parseMetrics(metrics));
        }
        List<SummaryDTO> otherResults = allResults.stream().filter(dto -> !dto.getRunId().equals(executionId.getValue())).collect(Collectors.toList());
        otherResults.add(summary);
        this.writeResult(otherResults);
        logger.info("Exiting : JsonExecutionResultRepository.updateResult()");
        return executionId;
    }

    private Map<String, String> parseMetrics(Configuration metrics) {
        if (!(metrics instanceof JsonConfiguration)) {
            JsonConfiguration configuration = (JsonConfiguration) metrics;
            return null;

        } else {
            throw new IllegalStateException("invalid metrics type");
        }
    }

    @Override
    public AppExecutionResult lookupResult(Id executionId) throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.SystemFailureException {
        logger.info("Executing : JsonExecutionResultRepository.lookupResult(executionId : {})", executionId);
        if (executionId == null) {
            logger.error("invalid arguments, executionId can not be null");
            throw new IllegalArgumentException("invalid arguments, executionId can not be null");
        }
        SummaryDTO summary = this.lookupSummary(executionId);
        logger.info("Exiting : JsonExecutionResultRepository.lookupResult()");
        return this.mapAppExecutionResult(summary);
    }

    @Override
    public List<AppExecutionResult> lookupResults(Id applicationId) throws StrangerExceptions.SystemFailureException {
        logger.info("Executing : JsonExecutionResultRepository.lookupResults(applicationId : {})", applicationId);
        if (applicationId == null) {
            logger.error("invalid arguments, applicationId can not be null");
            throw new IllegalArgumentException("invalid arguments, applicationId can not be null");
        }
        List<SummaryDTO> allResults = this.load();
        logger.info("Exiting : JsonExecutionResultRepository.lookupResults()");
        return allResults.stream().filter(dto -> dto.getAppId().equals(applicationId.getValue()))
                .map(this::mapAppExecutionResult).collect(Collectors.toList());
    }

    @Override
    public List<AppExecutionResult> lookupResults(Id applicationId, Set<AppExecutionResult.AppExecutionStatus> statuses) throws StrangerExceptions.SystemFailureException {
        logger.info("Executing : JsonExecutionResultRepository.lookupResults(applicationId : {}, statuses : {})", applicationId, statuses);
        if (applicationId == null || statuses == null || statuses.isEmpty()) {
            logger.error("invalid arguments, applicationId : {}, statuses : {}", applicationId, statuses);
            throw new IllegalArgumentException("invalid arguments, applicationId : " + applicationId + ", statuses : " + statuses);
        }
        List<SummaryDTO> allResults = this.load();
        logger.info("Exiting : JsonExecutionResultRepository.lookupResults()");
        return allResults.stream().filter(dto -> dto.getAppId().equals(applicationId.getValue()) && compareStatuses(dto.getAppExecutionStatus(), statuses))
                .map(this::mapAppExecutionResult).collect(Collectors.toList());

    }

    private boolean compareStatuses(String appExecutionStatus, Set<AppExecutionResult.AppExecutionStatus> statuses) {
        return statuses.stream().filter(st -> st.name().equals(appExecutionStatus)).count() > 0;
    }

    private List<SummaryDTO> load() throws StrangerExceptions.SystemFailureException {
        if (!this.summaryDirectory.exists()) {
            logger.info("creating directory - {}", this.summaryDirectory);
            this.summaryDirectory.mkdirs();
            this.summaryFile = new File(summaryDirectory, FILE_NAME);
            BufferedWriter bufferedWriter = null;
            try {
                this.summaryFile.createNewFile();
                bufferedWriter = new BufferedWriter(new FileWriter(this.summaryFile));
                bufferedWriter.write("[]");
                return new ArrayList<>();
            } catch (IOException e) {
                logger.error("error occurred while creating file", e);
                throw new StrangerExceptions.SystemFailureException("error occurred while creating file", e);
            } finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        throw new StrangerExceptions.SystemFailureException("error occurred while closing file", e);
                    }
                }
            }
        } else {
            this.summaryFile = new File(summaryDirectory, FILE_NAME);
            try {
                return summaryReader.readValue(this.summaryFile);
            } catch (IOException e) {
                throw new StrangerExceptions.SystemFailureException("error occurred while reading file", e);
            }
        }

    }

    private Id randomId() {
        return new StringId(UUID.randomUUID().toString());
    }


    private void writeResult(List<SummaryDTO> allResults) throws StrangerExceptions.SystemFailureException {
        try {
            this.summaryWriter.writeValue(this.summaryFile, allResults);
        } catch (IOException e) {
            throw new StrangerExceptions.SystemFailureException("error occurred while writing file", e);
        }
    }


    private AppExecutionResult mapAppExecutionResult(SummaryDTO summary) {
        AppExecutionResult.AppExecutionResultBuilder resultBuilder = new AppExecutionResult.AppExecutionResultBuilder()
                .withAppId(new StringId(summary.getAppId()))
                .withAppExecutionStatus(AppExecutionResult.AppExecutionStatus.getStatus(summary.appExecutionStatus))
                .withRunBy(summary.getRunBy())
                .withStartDate(summary.getStartDate())
                .withEngine(summary.getEngine())
                .withExecutionMessage(summary.getExecutionMessage())
                .withEndDate(summary.getEndDate())
                .withRunId(new StringId(summary.getRunId()));

        if (summary.getMetrics() != null) {
            JsonNode node = ConfigurationFactory.getObjectMapper().convertValue(summary.getMetrics(), JsonNode.class);
            resultBuilder = resultBuilder.withMetrics(new JsonConfiguration(node));
        }

        return resultBuilder.build();
    }


    private SummaryDTO lookupSummary(Id executionId) throws StrangerExceptions.ObjectNotFoundException, StrangerExceptions.SystemFailureException {
        List<SummaryDTO> allResults = this.load();
        Optional<SummaryDTO> summaryOption = allResults.stream().filter(dto -> dto.getRunId().equals(executionId.getValue())).findAny();
        if (!summaryOption.isPresent()) {
            logger.error("summary not found for id - {}", executionId);
            throw new StrangerExceptions.ObjectNotFoundException("summary not found for id - " + executionId);
        }
        return summaryOption.get();
    }


    private static class SummaryDTO {
        private String runId;
        private String appId;
        private String engine;
        private String runBy;

        private Date startDate;

        private Date endDate;

        private String appExecutionStatus;

        private String executionMessage;

        private Map<String, String> metrics;

        public SummaryDTO() {
        }

        public SummaryDTO(String runId, String appId, String engine, String runBy, Date startDate, String appExecutionStatus, String executionMessage) {
            this.runId = runId;
            this.appId = appId;
            this.engine = engine;
            this.runBy = runBy;
            this.startDate = startDate;
            this.appExecutionStatus = appExecutionStatus;
            this.executionMessage = executionMessage;
        }

        public String getRunId() {
            return runId;
        }

        public void setRunId(String runId) {
            this.runId = runId;
        }

        public String getEngine() {
            return engine;
        }

        public void setEngine(String engine) {
            this.engine = engine;
        }

        public String getRunBy() {
            return runBy;
        }

        public void setRunBy(String runBy) {
            this.runBy = runBy;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getAppExecutionStatus() {
            return appExecutionStatus;
        }

        public void setAppExecutionStatus(String appExecutionStatus) {
            this.appExecutionStatus = appExecutionStatus;
        }

        public String getExecutionMessage() {
            return executionMessage;
        }

        public void setExecutionMessage(String executionMessage) {
            this.executionMessage = executionMessage;
        }

        public Map<String, String> getMetrics() {
            return metrics;
        }

        public void setMetrics(Map<String, String> metrics) {
            this.metrics = metrics;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }
    }
}
