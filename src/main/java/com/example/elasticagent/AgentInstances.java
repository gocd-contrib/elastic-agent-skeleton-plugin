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

import com.example.elasticagent.models.AgentStatusReport;
import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.models.StatusReport;
import com.example.elasticagent.requests.CreateAgentRequest;


/**
 * Plugin implementors should implement these methods to interface to your cloud.
 * This interface is merely a suggestion for a very simple plugin. You may change it to your needs.
 */
public interface AgentInstances<T> {
    /**
     * This message is sent to request creation of an agent instance.
     * Implementations may, at their discretion choose to not spin up an agent instance.
     * <p>
     * So that instances created are auto-registered with the server, the agent instance MUST have an
     * <code>autoregister.properties</code> file.
     *
     * @param request   the request object
     */
    T create(CreateAgentRequest request) throws Exception;

    /**
     * This message is sent when the plugin needs to terminate the agent instance.
     *
     * @param agentId  the elastic agent id
     * @param clusterProfile the plugin cluster profile properties object
     */
    void terminate(String agentId, ClusterProfileProperties clusterProfile) throws Exception;

    /**
     * This message is sent from the {@link com.example.elasticagent.executors.ServerPingRequestExecutor}
     * to terminate instances that did not register with the server after a timeout. The timeout may be configurable and
     * set via the {@link ClusterProfileProperties} instance that is passed in.
     *
     * @param clusterProfile the plugin clusterProfile object
     * @param agents   the list of all the agents
     */
    void terminateUnregisteredInstances(ClusterProfileProperties clusterProfile, Agents agents) throws Exception;

    /**
     * This message is sent from the {@link com.example.elasticagent.executors.ServerPingRequestExecutor}
     * to filter out any new agents, that have registered before the timeout period. The timeout may be configurable and
     * set via the {@link ClusterProfileProperties} instance that is passed in.
     *
     * @param clusterProfile the plugin clusterProfile object
     * @param agents   the list of all the agents
     * @return a list of agent instances which were created after {@link ClusterProfileProperties#getAutoRegisterPeriod()} ago.
     */
    Agents instancesCreatedAfterTimeout(ClusterProfileProperties clusterProfile, Agents agents);

    /**
     * This message is sent after plugin initialization time so that the plugin may connect to the cloud provider
     * and fetch a list of all instances that have been spun up by this plugin (before the server was shut down).
     * This call should be should ideally remember if the agent instances are refreshed, and do nothing if instances
     * were previously refreshed.
     *
     * @param clusterProfile the cluster profile properties
     */
    void refreshAll(ClusterProfileProperties clusterProfile) throws Exception;

    /**
     * This
     * Returns an agent instance with the specified <code>id</code> or <code>null</code>, if the agent is not found.
     *
     * @param agentId the elastic agent id
     */
    T find(String agentId);

    /**
     * Finds an agent instance with the specified <code>jobIdentifier</code>
     * @param jobIdentifier The Job Identifier
     * @return An agent instance, or <code>null</code> if the agent is not found
     */
    T find(JobIdentifier jobIdentifier);

    /**
     * Get the status report from the agents
     * @param clusterProfile the cluster properties object
     * @return A StatusReport object
     * @throws Exception
     */
    StatusReport getStatusReport(ClusterProfileProperties clusterProfile) throws Exception;

    /**
     * Get the status report of an agent instance
     * @param clusterProfile The cluster profile properties object
     * @param agentInstance The agent instance
     * @return An AgentStatusReport object
     */
    AgentStatusReport getAgentStatusReport(ClusterProfileProperties clusterProfile, T agentInstance);
}

