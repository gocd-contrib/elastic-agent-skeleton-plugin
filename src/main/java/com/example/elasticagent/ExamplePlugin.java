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

package com.example.elasticagent;

import com.example.elasticagent.executors.*;
import com.example.elasticagent.requests.*;
import com.example.elasticagent.views.ViewBuilder;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.elasticagent.Constants.PLUGIN_IDENTIFIER;

@Extension
public class ExamplePlugin implements GoPlugin {

    public static final Logger LOG = Logger.getLoggerFor(ExamplePlugin.class);

    private PluginRequest pluginRequest;
    private AgentInstances agentInstances;
    private Map<String, AgentInstances> clusterSpecificAgentInstances;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor accessor) {
        pluginRequest = new PluginRequest(accessor);
        agentInstances = new ExampleAgentInstances();
        clusterSpecificAgentInstances = new HashMap<>();
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return PLUGIN_IDENTIFIER;
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) {
        try {
            switch (Request.fromString(request.requestName())) {
                case REQUEST_GET_ICON:
                    return new GetPluginSettingsIconExecutor().execute();
                case REQUEST_SHOULD_ASSIGN_WORK:
                    ShouldAssignWorkRequest shouldAssignWorkRequest = ShouldAssignWorkRequest.fromJSON(request.requestBody());
                    refreshInstancesForCluster(shouldAssignWorkRequest.clusterProperties());
                    return shouldAssignWorkRequest.executor(agentInstances).execute();

                case REQUEST_CREATE_AGENT:
                    CreateAgentRequest createAgentRequest = CreateAgentRequest.fromJSON(request.requestBody());
                    refreshInstancesForCluster(createAgentRequest.clusterProperties());
                    AgentInstances agentInstancesForCluster = getAgentInstancesForCluster(createAgentRequest.clusterProperties());
                    return createAgentRequest.executor(agentInstancesForCluster).execute();

                case REQUEST_SERVER_PING:
                    ServerPingRequest serverPingRequest = ServerPingRequest.fromJSON(request.requestBody());
                    refreshInstancesForAllClusters(serverPingRequest.allClusterProfileProperties());
                    return serverPingRequest.executor(clusterSpecificAgentInstances, pluginRequest).execute();

                case REQUEST_GET_ELASTIC_AGENT_PROFILE_METADATA:
                    return new GetProfileMetadataExecutor().execute();
                case REQUEST_GET_ELASTIC_AGENT_PROFILE_VIEW:
                    return new GetProfileViewExecutor().execute();
                case REQUEST_VALIDATE_ELASTIC_AGENT_PROFILE:
                    return ProfileValidateRequest.fromJSON(request.requestBody()).executor().execute();
                case REQUEST_JOB_COMPLETION:
                    refreshInstances();
                    return JobCompletionRequest.fromJSON(request.requestBody()).executor(agentInstances, pluginRequest).execute();
                case REQUEST_PLUGIN_STATUS_REPORT:
                    refreshInstances();
//                    return new PluginStatusReportExecutor(pluginRequest, agentInstances, ViewBuilder.instance()).execute();
                case REQUEST_AGENT_STATUS_REPORT:
                    refreshInstances();
                    return AgentStatusReportRequest.fromJSON(request.requestBody()).executor(pluginRequest, agentInstances, ViewBuilder.instance()).execute();
                case REQUEST_CAPABILITIES:
                    return new GetCapabilitiesExecutor().execute();
                default:
                    throw new UnhandledRequestTypeException(request.requestName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // todo: this needs to go
    private void refreshInstances() {
        try {
            agentInstances.refreshAll(pluginRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshInstancesForAllClusters(List<ClusterProfileProperties> listOfClusterProfileProperties) throws Exception {
        for (ClusterProfileProperties clusterProfileProperties : listOfClusterProfileProperties) {
            refreshInstancesForCluster(clusterProfileProperties);
        }
    }

    private void refreshInstancesForCluster(ClusterProfileProperties clusterProfileProperties) throws Exception {
        AgentInstances agentInstances = getAgentInstancesForCluster(clusterProfileProperties);
        agentInstances.refreshAll(clusterProfileProperties);
        clusterSpecificAgentInstances.put(clusterProfileProperties.uuid(), agentInstances);
    }

    private AgentInstances getAgentInstancesForCluster(ClusterProfileProperties clusterProfileProperties) {
        return clusterSpecificAgentInstances.get(clusterProfileProperties.uuid());
    }

}
