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

package com.example.elasticagent.executors;

import com.example.elasticagent.*;
import com.example.elasticagent.requests.ServerPingRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.*;

public class ServerPingRequestExecutor implements RequestExecutor {
    private final PluginRequest pluginRequest;
    private final Map<String, AgentInstances> clusterSpecificAgentInstances;
    private final ServerPingRequest serverPingRequest;

    public ServerPingRequestExecutor(Map<String, AgentInstances> clusterSpecificAgentInstances, ServerPingRequest serverPingRequest, PluginRequest pluginRequest) {

        this.clusterSpecificAgentInstances = clusterSpecificAgentInstances;
        this.serverPingRequest = serverPingRequest;
        this.pluginRequest = pluginRequest;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        List<ClusterProfileProperties> allClusterProfileProperties = serverPingRequest.allClusterProfileProperties();

        for (ClusterProfileProperties clusterProfileProperties : allClusterProfileProperties) {
            performCleanupForCluster(clusterProfileProperties, clusterSpecificAgentInstances.get(clusterProfileProperties.uuid()));
        }

        return DefaultGoPluginApiResponse.success("");
    }

    private void performCleanupForCluster(ClusterProfileProperties clusterProfileProperties, AgentInstances agentInstances) throws Exception {
        Agents allAgents = pluginRequest.listAgents();
        Set<Agent> missingAgents = new HashSet<>();

        for (Agent agent : allAgents.agents()) {
            if (agentInstances.find(agent.elasticAgentId()) == null) {
                missingAgents.add(agent);
            } else {
                missingAgents.remove(agent);
            }
        }

        Agents agentsToDisable = agentInstances.instancesCreatedAfterTimeout(clusterProfileProperties, allAgents);

        agentsToDisable.addAll(missingAgents);

        disableIdleAgents(agentsToDisable);

        allAgents = pluginRequest.listAgents();
        terminateDisabledAgents(allAgents, clusterProfileProperties, agentInstances);

        agentInstances.terminateUnregisteredInstances(clusterProfileProperties, allAgents);
    }

    private void disableIdleAgents(Agents agents) throws ServerRequestFailedException {
        pluginRequest.disableAgents(agents.findInstancesToDisable());
    }

    private void terminateDisabledAgents(Agents agents, ClusterProfileProperties pluginSettings, AgentInstances agentInstances) throws Exception {
        Collection<Agent> toBeDeleted = agents.findInstancesToTerminate();

        for (Agent agent : toBeDeleted) {
            agentInstances.terminate(agent.elasticAgentId(), pluginSettings);
        }

        pluginRequest.deleteAgents(toBeDeleted);
    }

}
