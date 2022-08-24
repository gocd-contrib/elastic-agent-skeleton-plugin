package com.example.elasticagent.requests;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.ClusterProfileProperties;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.executors.PluginStatusReportExecutor;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

import static com.example.elasticagent.ExamplePlugin.GSON;

public class PluginStatusReportRequest {
    @Expose
    protected List<ClusterProfileProperties> allClusterProfilesProperties;

    public PluginStatusReportRequest() {
    }

    public static PluginStatusReportRequest fromJSON(String json) {
        return GSON.fromJson(json, PluginStatusReportRequest.class);
    }

    public RequestExecutor executor(Map<String, AgentInstances> clusterSpecificAgentInstances, ViewBuilder instance) {
        return new PluginStatusReportExecutor(this, clusterSpecificAgentInstances, instance);
    }

    public List<ClusterProfileProperties> allClusterProfileProperties() {
        return allClusterProfilesProperties;
    }

    // Dummy implementation to show status report rendering; not relevant in a real implementation
    public List<ClusterProfileProperties> allClusterProfilePropertiesWithDefaultClusterProfile() {
        return allClusterProfilesProperties.isEmpty()
                ? List.of(new ClusterProfileProperties())
                : allClusterProfilesProperties;
    }
}
