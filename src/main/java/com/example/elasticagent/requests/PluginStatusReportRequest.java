package com.example.elasticagent.requests;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.executors.PluginStatusReportExecutor;
import com.example.elasticagent.views.ViewBuilder;

import java.util.Map;

public class PluginStatusReportRequest extends ServerPingRequest {
    public static PluginStatusReportRequest fromJSON(String json) {
        return (PluginStatusReportRequest) ServerPingRequest.fromJSON(json);
    }

    public RequestExecutor executor(Map<String, AgentInstances> clusterSpecificAgentInstances, ViewBuilder instance) {
        return new PluginStatusReportExecutor(this, clusterSpecificAgentInstances, instance);
    }
}
