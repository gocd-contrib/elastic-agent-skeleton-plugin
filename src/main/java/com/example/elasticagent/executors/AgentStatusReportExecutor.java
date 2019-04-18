package com.example.elasticagent.executors;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.ExampleInstance;
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.models.AgentStatusReport;
import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.requests.AgentStatusReportRequest;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.commons.lang3.StringUtils;

public class AgentStatusReportExecutor {
    private static final Logger LOG = Logger.getLoggerFor(AgentStatusReportExecutor.class);
    private final AgentStatusReportRequest request;
    private final AgentInstances<ExampleInstance> agentInstances;
    private final ViewBuilder viewBuilder;

    public AgentStatusReportExecutor(AgentStatusReportRequest request,
                                     AgentInstances<ExampleInstance> agentInstances, ViewBuilder viewBuilder) {
        this.request = request;
        this.agentInstances = agentInstances;
        this.viewBuilder = viewBuilder;
    }

    public GoPluginApiResponse execute() throws Exception {
        String elasticAgentId = request.getElasticAgentId();
        JobIdentifier jobIdentifier = request.getJobIdentifier();
        LOG.info(String.format("[status-report] Generating status report for agent: %s with job: %s", elasticAgentId, jobIdentifier));

        try {
            if (StringUtils.isNotBlank(elasticAgentId)) {
                return getStatusReportUsingElasticAgentId(elasticAgentId);
            }
            return getStatusReportUsingJobIdentifier(jobIdentifier);
        } catch (Exception e) {
            LOG.debug("Exception while generating agent status report", e);
            final String statusReportView = viewBuilder.build("error-template");
            return constructResponseForReport(statusReportView);
        }
    }

    private GoPluginApiResponse getStatusReportUsingJobIdentifier(JobIdentifier jobIdentifier) throws Exception {
        ExampleInstance agentInstance = agentInstances.find(jobIdentifier);
        if (agentInstance != null) {
            AgentStatusReport agentStatusReport = agentInstances.getAgentStatusReport(request.clusterProperties(), agentInstance);
            final String statusReportView = viewBuilder.build("status-report-template", agentStatusReport);
            return constructResponseForReport(statusReportView);
        }
        return containerNotFoundApiResponse(jobIdentifier);
    }

    private GoPluginApiResponse getStatusReportUsingElasticAgentId(String elasticAgentId) throws Exception {
        ExampleInstance agentInstance = agentInstances.find(elasticAgentId);
        if (agentInstance != null) {
            AgentStatusReport agentStatusReport = agentInstances.getAgentStatusReport(request.clusterProperties(), agentInstance);
            final String statusReportView = viewBuilder.build("status-report-template", agentStatusReport);
            return constructResponseForReport(statusReportView);
        }
        return containerNotFoundApiResponse(elasticAgentId);
    }

    private GoPluginApiResponse constructResponseForReport(String statusReportView) {
        JsonObject responseJSON = new JsonObject();
        responseJSON.addProperty("view", statusReportView);
        return DefaultGoPluginApiResponse.success(responseJSON.toString());
    }

    private GoPluginApiResponse containerNotFoundApiResponse(JobIdentifier jobIdentifier) {
        final String statusReportView = viewBuilder.build("not-running-template");
        return constructResponseForReport(statusReportView);
    }

    private GoPluginApiResponse containerNotFoundApiResponse(String elasticAgentId) {
        final String statusReportView = viewBuilder.build("not-running-template");
        return constructResponseForReport(statusReportView);
    }
}
