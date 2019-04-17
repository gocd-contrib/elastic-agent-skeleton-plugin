package com.example.elasticagent.executors;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.ExampleInstance;
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.requests.JobCompletionRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class JobCompletionRequestExecutor implements RequestExecutor {
    private final JobCompletionRequest jobCompletionRequest;
    private final AgentInstances<ExampleInstance> agentInstances;

    public JobCompletionRequestExecutor(JobCompletionRequest jobCompletionRequest, AgentInstances<ExampleInstance> agentInstances) {
        this.jobCompletionRequest = jobCompletionRequest;
        this.agentInstances = agentInstances;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        agentInstances.terminate(jobCompletionRequest.getElasticAgentId(), jobCompletionRequest.clusterProperties());
        return new DefaultGoPluginApiResponse(200);
    }
}
