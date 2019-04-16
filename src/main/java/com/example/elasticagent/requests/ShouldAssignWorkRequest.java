/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.elasticagent.requests;

import com.example.elasticagent.*;
import com.example.elasticagent.executors.ShouldAssignWorkRequestExecutor;
import com.example.elasticagent.models.JobIdentifier;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * Represents the {@link com.example.elasticagent.Request#REQUEST_SHOULD_ASSIGN_WORK} message.
 */
public class ShouldAssignWorkRequest {
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private Agent agent;
    private String environment;
    private JobIdentifier jobIdentifier;
    private Map<String, String> elasticAgentProfileProperties;
    private ClusterProfileProperties clusterProfileProperties;


    public ShouldAssignWorkRequest(Agent agent, String environment, JobIdentifier jobIdentifier, Map<String, String> elasticAgentProfileProperties, ClusterProfileProperties clusterProfileProperties) {
        this.agent = agent;
        this.environment = environment;
        this.jobIdentifier = jobIdentifier;
        this.elasticAgentProfileProperties = elasticAgentProfileProperties;
        this.clusterProfileProperties = clusterProfileProperties;
    }

    public ShouldAssignWorkRequest() {
    }

    public Agent agent() {
        return agent;
    }

    public String environment() {
        return environment;
    }

    public JobIdentifier jobIdentifier() {
        return jobIdentifier;
    }

    public Map<String, String> profileProperties() {
        return elasticAgentProfileProperties;
    }

    public ClusterProfileProperties clusterProperties() {
        return clusterProfileProperties;
    }

    public static ShouldAssignWorkRequest fromJSON(String json) {
        return GSON.fromJson(json, ShouldAssignWorkRequest.class);
    }

    public RequestExecutor executor(AgentInstances<ExampleInstance> agentInstances) {
        return new ShouldAssignWorkRequestExecutor(this, agentInstances);
    }
}
