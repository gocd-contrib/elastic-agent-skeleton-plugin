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

import com.example.elasticagent.ClusterProfileProperties;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static java.util.Collections.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateAgentRequestTest {

    @Test
    public void shouldDeserializeFromJSON() throws Exception {
        String json = "{\n" +
                "  \"auto_register_key\": \"auto-registration-key\",\n" +
                "  \"elastic_agent_profile_properties\": {\n" +
                "    \"Image\": \"alpine:latest\"\n" +
                "  },\n" +
                "  \"cluster_profile_properties\": {\n" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"test\",\n" +
                "    \"api_key\": \"test-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  },\n" +
                "  \"environment\": \"test-env\",\n" +
                "  \"job_identifier\": {\n" +
                "    \"pipeline_name\": \"up42\",\n" +
                "    \"pipeline_label\": \"Test\",\n" +
                "    \"pipeline_counter\": 2,\n" +
                "    \"stage_name\": \"up42_stage\",\n" +
                "    \"stage_counter\": \"10\",\n" +
                "    \"job_name\": \"up42_job\",\n" +
                "    \"job_id\": -1\n" +
                "  }\n" +
                "}";

        ClusterProfileProperties expectedClusterProperties = new ClusterProfileProperties(
                "https://localhost:8154/go",
                "20m",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        CreateAgentRequest request = CreateAgentRequest.fromJSON(json);
        assertThat(request.autoRegisterKey(), equalTo("auto-registration-key"));
        assertThat(request.environment(), equalTo("test-env"));

        assertThat(request.profileProperties(), Matchers.equalTo(singletonMap("Image", "alpine:latest")));
        assertThat(request.clusterProperties(), Matchers.equalTo(expectedClusterProperties));
    }
}
