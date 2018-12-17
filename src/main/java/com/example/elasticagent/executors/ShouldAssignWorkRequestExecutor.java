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
import com.example.elasticagent.ExampleInstance;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.requests.ShouldAssignWorkRequest;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class ShouldAssignWorkRequestExecutor implements RequestExecutor {
    private final AgentInstances<ExampleInstance> agentInstances;
    private final ShouldAssignWorkRequest request;
    
	public static final Logger LOG = Logger.getLoggerFor(ShouldAssignWorkRequestExecutor.class);
	
    public ShouldAssignWorkRequestExecutor(ShouldAssignWorkRequest request, AgentInstances<ExampleInstance> agentInstances) {
        this.request = request;
        this.agentInstances = agentInstances;
    }

    @Override
    public GoPluginApiResponse execute() {
    	LOG.info("Should assign work - Agent: " + request.agent().elasticAgentId());
    	LOG.info("Should assign work - JobId: " + request.jobIdentifier());
        ExampleInstance instance = agentInstances.find(request.agent().elasticAgentId());

        if (instance == null) {
        	LOG.info("Should assign work - false (null)");
            return DefaultGoPluginApiResponse.success("false");
        }
        LOG.info("Should assign work - found: " + instance.name());
        LOG.info("Should assign work - FJ Id: " + instance.jobIdentifier());
        if (instance.jobIdentifier().equals(request.jobIdentifier())) {
        	LOG.info("Should assign work - True");
            return DefaultGoPluginApiResponse.success("true");
        }
        LOG.info("Should assign work - false (job id does not match)");
        return DefaultGoPluginApiResponse.success("false");
    }
}
