package org.stranger.data.store.repo.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stranger.common.exception.StrangerExceptions;
import org.stranger.common.model.application.AppExecutionResult;
import org.stranger.common.model.configuration.Configuration;
import org.stranger.common.model.configuration.ConfigurationFactory;
import org.stranger.common.model.configuration.impl.JsonConfiguration;
import org.stranger.common.model.id.Id;
import org.stranger.common.model.id.StringId;
import org.stranger.data.store.repo.ExecutionResultRepository;
import scala.reflect.internal.Trees;

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
    public Id storeResult(Id appId, AppExecutionResult.AppExecutionStatus executionStatus, String message, String runBy, Date startDate) {
        List<SummaryDTO> allResults = this.load();
        String id = UUID.randomUUID().toString();
        SummaryDTO summaryDTO = new SummaryDTO(id,appId.getValue(), "spark", runBy, startDate, executionStatus.name(), message);
        allResults.add(summaryDTO);
        this.writeResult(allResults);
        return new StringId(id);
    }

    @Override
    public Id updateResult(Id executionId, AppExecutionResult.AppExecutionStatus executionStatus, String message, Date endDate, Configuration metrics) throws StrangerExceptions.ObjectNotFoundException {

        List<SummaryDTO> allResults = this.load();
        SummaryDTO summary = this.lookupSummary(executionId);
        summary.setAppExecutionStatus(executionStatus.name());
        summary.setExecutionMessage(message);
        summary.setEndDate(endDate);

        List<SummaryDTO> otherResults = allResults.stream().filter(dto -> !dto.getRunId().equals(executionId.getValue())).collect(Collectors.toList());
        otherResults.add(summary);
        this.writeResult(otherResults);
        return null;
    }

    @Override
    public AppExecutionResult lookupResult(Id executionId) throws StrangerExceptions.ObjectNotFoundException {
        SummaryDTO summary = this.lookupSummary(executionId);
        return this.mapAppExecutionResult(summary);
    }

    @Override
    public List<AppExecutionResult> lookupResults(Id applicationId) {
        List<SummaryDTO> allResults = this.load();
        return allResults.stream().filter(dto -> dto.getAppId().equals(applicationId.getValue()))
                .map(this::mapAppExecutionResult).collect(Collectors.toList());
    }

    @Override
    public List<AppExecutionResult> lookupResults(Id applicationId, Set<AppExecutionResult.AppExecutionStatus> statuses) {
        List<SummaryDTO> allResults = this.load();
        return allResults.stream().filter(dto -> dto.getAppId().equals(applicationId.getValue()) && compareStatuses(dto.getAppExecutionStatus(), statuses))
                .map(this::mapAppExecutionResult).collect(Collectors.toList());

    }

    private boolean compareStatuses(String appExecutionStatus, Set<AppExecutionResult.AppExecutionStatus> statuses) {
        return statuses.stream().filter(st -> st.name().equals(appExecutionStatus)).count() > 0;
    }

    private List<SummaryDTO> load() {
        JsonConfiguration summaryList = null;
        if (!this.summaryDirectory.exists()) {
            this.summaryDirectory.mkdirs();
            this.summaryFile = new File(summaryDirectory, FILE_NAME);

            try {
                this.summaryFile.createNewFile();

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.summaryFile));
                bufferedWriter.write("[]");
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.summaryFile = new File(summaryDirectory, FILE_NAME);
        }
        try {
            return summaryReader.readValue(this.summaryFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void writeResult(List<SummaryDTO> allResults) {
        try {
            this.summaryWriter.writeValue(this.summaryFile, allResults);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private AppExecutionResult mapAppExecutionResult(SummaryDTO summary) {
        return null;
    }


    private SummaryDTO lookupSummary(Id executionId) throws StrangerExceptions.ObjectNotFoundException {
        List<SummaryDTO> allResults = this.load();
        Optional<SummaryDTO> summaryOption = allResults.stream().filter(dto -> dto.getRunId().equals(executionId.getValue())).findAny();
        if (!summaryOption.isPresent()) {
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
