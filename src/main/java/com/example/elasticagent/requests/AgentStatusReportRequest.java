package com.example.elasticagent.requests;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.ClusterProfileProperties;
import com.example.elasticagent.executors.AgentStatusReportExecutor;
import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.Objects;

public class AgentStatusReportRequest {
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Expose
    private String elasticAgentId;

    @Expose
    private JobIdentifier jobIdentifier;

    @Expose
    private ClusterProfileProperties clusterProfileProperties;

    public AgentStatusReportRequest() {
    }

    public AgentStatusReportRequest(String elasticAgentId, JobIdentifier jobIdentifier, ClusterProfileProperties clusterProfileProperties) {
        this.elasticAgentId = elasticAgentId;
        this.jobIdentifier = jobIdentifier;
        this.clusterProfileProperties = clusterProfileProperties;
    }

    public static AgentStatusReportRequest fromJSON(String json) {
        return GSON.fromJson(json, AgentStatusReportRequest.class);
    }

    public String getElasticAgentId() {
        return elasticAgentId;
    }

    public JobIdentifier getJobIdentifier() {
        return jobIdentifier;
    }

    public AgentStatusReportExecutor executor(AgentInstances agentInstances, ViewBuilder viewBuilder) {
        return new AgentStatusReportExecutor(this, agentInstances, viewBuilder);
    }

    public ClusterProfileProperties clusterProperties() {
        return clusterProfileProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgentStatusReportRequest that = (AgentStatusReportRequest) o;

        if (elasticAgentId != null ? !elasticAgentId.equals(that.elasticAgentId) : that.elasticAgentId != null)
            return false;
        if (jobIdentifier != null ? !jobIdentifier.equals(that.jobIdentifier) : that.jobIdentifier != null)
            return false;
        return clusterProfileProperties != null ? clusterProfileProperties.equals(that.clusterProfileProperties) : that.clusterProfileProperties == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(elasticAgentId, jobIdentifier, clusterProfileProperties);
    }


    @Override
    public String toString() {
        return "AgentStatusReportRequest{" +
                "elasticAgentId='" + elasticAgentId + '\'' +
                ", jobIdentifier=" + jobIdentifier +
                ", clusterProfileProperties=" + clusterProfileProperties +
                '}';
    }
}
