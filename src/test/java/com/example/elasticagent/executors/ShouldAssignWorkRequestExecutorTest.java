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
import com.example.elasticagent.models.JobIdentifierMother;
import com.example.elasticagent.requests.CreateAgentRequest;
import com.example.elasticagent.requests.ShouldAssignWorkRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ShouldAssignWorkRequestExecutorTest {

    private AgentInstances<ExampleInstance> agentInstances;
    private ExampleInstance instance;
    private final String environment = "production";
    private Map<String, String> profileProperties = new HashMap<>();
    private ClusterProfile clusterProperties = new ClusterProfile();
    private final JobIdentifier jobIdentifier = JobIdentifierMother.get();

    @BeforeEach
    public void setUp() throws Exception {
        agentInstances = new ExampleAgentInstances();
        profileProperties.put("foo", "bar");
        profileProperties.put("Image", "gocdcontrib/ubuntu-docker-elastic-agent");
        instance = agentInstances.create(new CreateAgentRequest(UUID.randomUUID().toString(), profileProperties, clusterProperties, environment, jobIdentifier));
    }

    @Test
    public void shouldAssignWorkToContainerWithSameJobIdentifier() {
        ShouldAssignWorkRequest request = new ShouldAssignWorkRequest(new Agent(instance.name(), null, null, null), environment, jobIdentifier, null, null);
        GoPluginApiResponse response = new ShouldAssignWorkRequestExecutor(request, agentInstances).execute();
        assertThat(response.responseCode(), is(200));
        assertThat(response.responseBody(), is("true"));
    }

    @Test
    public void shouldNotAssignWorkToContainerWithDifferentJobIdentifier() {
        JobIdentifier otherJobId = new JobIdentifier("up42", 2L, "foo", "stage", "1", "job", 2L);
        ShouldAssignWorkRequest request = new ShouldAssignWorkRequest(new Agent(instance.name(), null, null, null), environment, otherJobId, null, null);
        GoPluginApiResponse response = new ShouldAssignWorkRequestExecutor(request, agentInstances).execute();
        assertThat(response.responseCode(), is(200));
        assertThat(response.responseBody(), is("false"));
    }

    @Test
    public void shouldNotAssignWorkIfInstanceIsNotFound() {
        ShouldAssignWorkRequest request = new ShouldAssignWorkRequest(new Agent("unknown-name", null, null, null), environment, jobIdentifier, null, null);
        GoPluginApiResponse response = new ShouldAssignWorkRequestExecutor(request, agentInstances).execute();
        assertThat(response.responseCode(), is(200));
        assertThat(response.responseBody(), is("false"));
    }
}
