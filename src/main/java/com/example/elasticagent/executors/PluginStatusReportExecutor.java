package com.example.elasticagent.executors;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.ClusterProfileProperties;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.models.StatusReport;
import com.example.elasticagent.requests.PluginStatusReportRequest;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Make changes as needed
public class PluginStatusReportExecutor implements RequestExecutor {

    private final PluginStatusReportRequest request;
    private final Map<String, AgentInstances> allClusterInstances;
    private final ViewBuilder viewBuilder;
    private static final Logger LOG = Logger.getLoggerFor(AgentStatusReportExecutor.class);

    public PluginStatusReportExecutor(PluginStatusReportRequest request, Map<String, AgentInstances> allClusterInstances, ViewBuilder viewBuilder) {
        this.request = request;
        this.allClusterInstances = allClusterInstances;
        this.viewBuilder = viewBuilder;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        LOG.info("[status-report] Generating status report");

        List<String> reports = new ArrayList<>();

        for (ClusterProfileProperties profile : request.allClusterProfileProperties()) {
            AgentInstances agentInstances = allClusterInstances.get(profile.uuid());
            StatusReport statusReport = agentInstances.getStatusReport(profile);
            reports.add(viewBuilder.build("status-report-template", statusReport));
        }

        // aggregate reports for different cluster into one
        JsonObject responseJSON = new JsonObject();
        responseJSON.addProperty("view", reports.stream().collect(Collectors.joining("<hr/>")));
        return DefaultGoPluginApiResponse.success(responseJSON.toString());
    }
}
