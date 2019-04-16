package com.example.elasticagent.executors;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.models.StatusReport;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class StatusReportExecutor implements RequestExecutor {

    private final PluginRequest pluginRequest;
    private final AgentInstances agentInstances;
    private final ViewBuilder viewBuilder;
    private static final Logger LOG = Logger.getLoggerFor(AgentStatusReportExecutor.class);

    public StatusReportExecutor(PluginRequest pluginRequest, AgentInstances agentInstances, ViewBuilder viewBuilder) {
        this.pluginRequest = pluginRequest;
        this.agentInstances = agentInstances;
        this.viewBuilder = viewBuilder;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        LOG.info("[status-report] Generating status report");

        StatusReport statusReport = agentInstances.getStatusReport(pluginRequest.getPluginSettings());

        final String statusReportView = viewBuilder.build("status-report-template", statusReport);

        JsonObject responseJSON = new JsonObject();
        responseJSON.addProperty("view", statusReportView);

        return DefaultGoPluginApiResponse.success(responseJSON.toString());
    }
}
