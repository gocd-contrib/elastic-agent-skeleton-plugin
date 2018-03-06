package com.example.elasticagent.executors;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.RequestExecutor;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class StatusReportExecutor implements RequestExecutor {

    private final PluginRequest pluginRequest;
    private final AgentInstances agentInstances;

    public StatusReportExecutor(PluginRequest pluginRequest, AgentInstances agentInstances) {
        this.pluginRequest = pluginRequest;
        this.agentInstances = agentInstances;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        JsonObject responseJSON = new JsonObject();
        responseJSON.addProperty("view", "Example plugin status report view");

        return DefaultGoPluginApiResponse.success(responseJSON.toString());
    }
}
