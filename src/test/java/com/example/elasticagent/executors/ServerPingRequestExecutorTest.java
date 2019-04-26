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
import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.requests.CreateAgentRequest;
import com.example.elasticagent.requests.ServerPingRequest;
import org.joda.time.Period;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.*;

import static com.example.elasticagent.Agent.ConfigState.Disabled;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class ServerPingRequestExecutorTest extends BaseTest {

    @Test
    public void testShouldDisableIdleAgents() throws Exception {
        String agentId = UUID.randomUUID().toString();
        final Agents agents = new Agents(Arrays.asList(new Agent(agentId, Agent.AgentState.Idle, Agent.BuildState.Idle, Agent.ConfigState.Enabled)));
        PluginRequest pluginRequest = mock(PluginRequest.class);
        ClusterProfileProperties clusterProfileProperties = mock(ClusterProfileProperties.class);

        when(clusterProfileProperties.uuid()).thenReturn("ffff");
        when(pluginRequest.listAgents()).thenReturn(agents);
        verifyNoMoreInteractions(pluginRequest);

        final Map<String, AgentInstances> clusterSpecificInstance = Collections.singletonMap("ffff", new ExampleAgentInstances());
        final Collection<Agent> values = agents.agents();
        final ServerPingRequest serverPingRequest = new ServerPingRequest(singletonList(clusterProfileProperties));

        new ServerPingRequestExecutor(clusterSpecificInstance, serverPingRequest, pluginRequest).execute();
        verify(pluginRequest).disableAgents(argThat(collectionMatches(values)));
    }

    private ArgumentMatcher<Collection<Agent>> collectionMatches(final Collection<Agent> values) {
        return argument -> new ArrayList<>(argument).equals(new ArrayList<>(values));
    }

    @Test
    public void testShouldTerminateDisabledAgents() throws Exception {
        String agentId = UUID.randomUUID().toString();
        final Agents agents = new Agents(Arrays.asList(new Agent(agentId, Agent.AgentState.Idle, Agent.BuildState.Idle, Disabled)));

        ClusterProfileProperties clusterProfileProperties = mock(ClusterProfileProperties.class);
        PluginRequest pluginRequest = mock(PluginRequest.class);

        when(clusterProfileProperties.uuid()).thenReturn("ffff");
        when(pluginRequest.listAgents()).thenReturn(agents);
        verifyNoMoreInteractions(pluginRequest);

        final Map<String, AgentInstances> clusterSpecificInstance = Collections.singletonMap("ffff", new ExampleAgentInstances());
        final ServerPingRequest serverPingRequest = new ServerPingRequest(singletonList(clusterProfileProperties));

        new ServerPingRequestExecutor(clusterSpecificInstance, serverPingRequest, pluginRequest).execute();
        final Collection<Agent> values = agents.agents();
        verify(pluginRequest).deleteAgents(argThat(collectionMatches(values)));
    }

    @Test
    public void testShouldTerminateInstancesThatNeverAutoRegistered() throws Exception {
        ClusterProfileProperties clusterProfileProperties = mock(ClusterProfileProperties.class);
        PluginRequest pluginRequest = mock(PluginRequest.class);

        when(clusterProfileProperties.uuid()).thenReturn("ffff");
        when(pluginRequest.listAgents()).thenReturn(new Agents());
        verifyNoMoreInteractions(pluginRequest);

        ExampleAgentInstances agentInstances = new ExampleAgentInstances();
        agentInstances.clock = new Clock.TestClock().forward(Period.minutes(11));
        ExampleInstance container = agentInstances.create(new CreateAgentRequest(null, new HashMap<>(), new ClusterProfileProperties(), null, new JobIdentifier()));

        final Map<String, AgentInstances> clusterSpecificInstance = Collections.singletonMap("ffff", agentInstances);
        final ServerPingRequest serverPingRequest = new ServerPingRequest(singletonList(clusterProfileProperties));

        new ServerPingRequestExecutor(clusterSpecificInstance, serverPingRequest, pluginRequest).execute();

        assertFalse(agentInstances.hasInstance(container.name()));
    }

    @Test
    public void shouldDeleteAgentFromConfigWhenCorrespondingContainerIsNotPresent() throws Exception {
        ClusterProfileProperties clusterProfileProperties = mock(ClusterProfileProperties.class);
        PluginRequest pluginRequest = mock(PluginRequest.class);

        when(clusterProfileProperties.uuid()).thenReturn("ffff");
        when(pluginRequest.listAgents()).thenReturn(new Agents(Arrays.asList(new Agent("foo", Agent.AgentState.Idle, Agent.BuildState.Idle, Agent.ConfigState.Enabled))));
        verifyNoMoreInteractions(pluginRequest);

        final Map<String, AgentInstances> clusterSpecificInstance = Collections.singletonMap("ffff", new ExampleAgentInstances());
        final ServerPingRequest serverPingRequest = new ServerPingRequest(singletonList(clusterProfileProperties));

        new ServerPingRequestExecutor(clusterSpecificInstance, serverPingRequest, pluginRequest).execute();
    }
}
