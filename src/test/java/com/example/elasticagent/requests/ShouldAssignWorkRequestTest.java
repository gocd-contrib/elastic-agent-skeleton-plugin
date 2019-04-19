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

import com.example.elasticagent.Agent;
import com.example.elasticagent.ClusterProfile;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ShouldAssignWorkRequestTest {

    @Test
    public void shouldDeserializeFromJSON() throws Exception {
        String json = "{\n" +
                "  \"environment\": \"prod\",\n" +
                "  \"agent\": {\n" +
                "    \"agent_id\": \"42\",\n" +
                "    \"agent_state\": \"Idle\",\n" +
                "    \"build_state\": \"Idle\",\n" +
                "    \"config_state\": \"Enabled\"\n" +
                "  },\n" +
                "  \"elastic_agent_profile_properties\": {\n" +
                "    \"property_name1\": \"property_value1\"\n" +
                "  },\n" +
                "  \"cluster_profile_properties\": {\n" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"test\",\n" +
                "    \"api_key\": \"test-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  }\n" +
                "}";

        ClusterProfile expectedClusterProperties = new ClusterProfile(
                "https://localhost:8154/go",
                "20m",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        ShouldAssignWorkRequest request = ShouldAssignWorkRequest.fromJSON(json);
        assertThat(request.environment(), equalTo("prod"));
        assertThat(request.agent(), equalTo(new Agent("42", Agent.AgentState.Idle, Agent.BuildState.Idle, Agent.ConfigState.Enabled)));
        Map<String, String> expectedProfileProperties = Collections.singletonMap("property_name1", "property_value1");

        assertThat(request.profileProperties(), Matchers.equalTo(expectedProfileProperties));
        assertThat(request.clusterProperties(), Matchers.equalTo(expectedClusterProperties));
    }
}
