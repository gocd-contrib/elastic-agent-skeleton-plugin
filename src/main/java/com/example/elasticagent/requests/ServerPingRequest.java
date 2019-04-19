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
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.executors.ServerPingRequestExecutor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

public class ServerPingRequest {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private List<ClusterProfile> allClusterProfileProperties;

    public ServerPingRequest() {
    }

    public ServerPingRequest(List<ClusterProfile> profileList) {
        this.allClusterProfileProperties = profileList;
    }

    public static ServerPingRequest fromJSON(String json) {
        return GSON.fromJson(json, ServerPingRequest.class);
    }

    public List<ClusterProfile> allClusterProfileProperties() {
        return allClusterProfileProperties;
    }

    public RequestExecutor executor(Map<String, AgentInstances> clusterSpecificAgentInstances, PluginRequest pluginRequest) {
        return new ServerPingRequestExecutor(clusterSpecificAgentInstances, this, pluginRequest);
    }
}
