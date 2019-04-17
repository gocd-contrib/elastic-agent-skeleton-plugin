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
    private final PluginRequest pluginRequest;

    public JobCompletionRequestExecutor(JobCompletionRequest jobCompletionRequest, AgentInstances<ExampleInstance> agentInstances, PluginRequest pluginRequest) {
        this.jobCompletionRequest = jobCompletionRequest;
        this.agentInstances = agentInstances;
        this.pluginRequest = pluginRequest;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        //todo: fix this
        agentInstances.terminate(jobCompletionRequest.getElasticAgentId(), null);
        return new DefaultGoPluginApiResponse(200);
    }
}
