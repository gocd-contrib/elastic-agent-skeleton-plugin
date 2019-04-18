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

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.ClusterProfileProperties;
import com.example.elasticagent.ExampleAgentInstances;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.requests.ClusterProfileChangedRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Map;

public class ClusterProfileChangedRequestExecutor implements RequestExecutor {
    private final Map<String, AgentInstances> allClusterInstances;
    private final ClusterProfileChangedRequest request;

    public ClusterProfileChangedRequestExecutor(ClusterProfileChangedRequest request, Map<String, AgentInstances> allClusterInstance) {
        this.request = request;
        this.allClusterInstances = allClusterInstance;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        switch(request.changeStatus()) {
            case CREATED:
                handleCreate(request, allClusterInstances);
                break;
            case UPDATED:
                handleUpdate(request, allClusterInstances);
                break;
            case DELETED:
                handleDelete(request, allClusterInstances);
                break;
        }
        return new DefaultGoPluginApiResponse(200);
    }

    private void handleCreate(ClusterProfileChangedRequest request, Map<String, AgentInstances> allClusterInstances) {
        ClusterProfileProperties newlyCreatedCluster = request.clusterProperties();
        // Create new cluster on cloud
        allClusterInstances.put(newlyCreatedCluster.uuid(), new ExampleAgentInstances());
    }

    private void handleDelete(ClusterProfileChangedRequest request, Map<String, AgentInstances> allClusterInstances) {
        String clusterToDelete = request.clusterProperties().uuid();
        AgentInstances clusterInstancesToDelete = allClusterInstances.get(clusterToDelete);

        // terminate all agent instances from a cluster

        allClusterInstances.remove(clusterToDelete);
    }

    private void handleUpdate(ClusterProfileChangedRequest request, Map<String, AgentInstances> allClusterInstances) {
        ClusterProfileProperties newCluster = request.clusterProperties();
        ClusterProfileProperties oldCluster = request.oldClusterProperties();

        AgentInstances oldClusterInstances = allClusterInstances.get(oldCluster.uuid());

        // terminate from old cluster and create instances on new cluster

        allClusterInstances.put(newCluster.uuid(), new ExampleAgentInstances());
    }
}
