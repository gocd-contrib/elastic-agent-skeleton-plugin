package com.example.elasticagent.executors;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.models.StatusReport;
import com.example.elasticagent.requests.ClusterStatusReportRequest;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.example.elasticagent.ExamplePlugin.LOG;

public class ClusterStatusReportExecutor implements RequestExecutor {

    private final ClusterStatusReportRequest clusterStatusReportRequest;
    private final AgentInstances agentInstances;
    private final ViewBuilder viewBuilder;

    public ClusterStatusReportExecutor(ClusterStatusReportRequest clusterStatusReportRequest, AgentInstances agentInstances, ViewBuilder viewBuilder) {
        this.clusterStatusReportRequest = clusterStatusReportRequest;
        this.agentInstances = agentInstances;
        this.viewBuilder = viewBuilder;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        LOG.info("[status-report] Generating status report");

        StatusReport statusReport = agentInstances.getStatusReport(clusterStatusReportRequest.getClusterProfile());

        final String statusReportView = viewBuilder.build("status-report.template", statusReport);

        JsonObject responseJSON = new JsonObject();
        responseJSON.addProperty("view", statusReportView);

        return DefaultGoPluginApiResponse.success(responseJSON.toString());
    }
}