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
import com.example.elasticagent.executors.MigrateConfigRequestExecutor;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

import static com.example.elasticagent.ExamplePlugin.GSON;

public class MigrateConfigPayload {
    @Expose
    private PluginSettings pluginSettings;

    @Expose
    private List<Map<String, String>> elasticAgentProfiles;

    @Expose
    private List<ClusterProfile> clusterProfiles;

    public MigrateConfigPayload() {
    }

    public MigrateConfigPayload(PluginSettings pluginSettings, List<Map<String, String>> elasticAgentProfileProperties, List<ClusterProfile> clusterProfiles) {
        this.pluginSettings = pluginSettings;
        this.elasticAgentProfiles = elasticAgentProfileProperties;
        this.clusterProfiles = clusterProfiles;
    }

    public static MigrateConfigPayload fromJSON(String json) {
        return GSON.fromJson(json, MigrateConfigPayload.class);
    }

    public RequestExecutor executor(Map<String, AgentInstances> allAgentInstances) {
        return new MigrateConfigRequestExecutor(this);
    }

    public PluginSettings pluginSettings() {
        return pluginSettings;
    }

    public List<Map<String, String>> elasticAgentProfileProperties() {
        return elasticAgentProfiles;
    }

    public List<ClusterProfile> clusterProfiles() {
        return clusterProfiles;
    }
}
