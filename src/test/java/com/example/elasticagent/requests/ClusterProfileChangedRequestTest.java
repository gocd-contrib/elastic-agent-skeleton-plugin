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

import com.example.elasticagent.ClusterProfile;
import com.example.elasticagent.requests.ClusterProfileChangedRequest.ChangeStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.example.elasticagent.requests.ClusterProfileChangedRequest.ChangeStatus.CREATED;
import static com.example.elasticagent.requests.ClusterProfileChangedRequest.ChangeStatus.DELETED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ClusterProfileChangedRequestTest {

    class CreateAndDelete {

    }
    @ParameterizedTest
    @MethodSource("inputChangeStatus")
    public void shouldDeserializeCreateAndDeleteClusterChangeFromJSON(String status, ChangeStatus changeStatus) throws Exception {
        String json = "{\n" +
                "  \"status\": \"" + status + "\",\n" +
                "  \"cluster_profiles_properties\": {\n" +
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

        ClusterProfileChangedRequest request = ClusterProfileChangedRequest.fromJSON(json);
        assertThat(request.changeStatus(), equalTo(changeStatus));
        assertThat(request.clusterProperties(), Matchers.equalTo(expectedClusterProperties));
    }

    private static Stream<Arguments> inputChangeStatus() {
        return Stream.of(
                Arguments.of("created", CREATED),
                Arguments.of("deleted", DELETED)
        );
    }


    @Test
    public void shouldDeserializeUpdateClusterChangeFromJSON() throws Exception {
        String json = "{\n" +
                "  \"status\": \"updated\",\n" +
                "  \"cluster_profiles_properties\": {\n" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"prod\",\n" +
                "    \"api_key\": \"prod-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  },\n" +
                "  \"old_cluster_profiles_properties\": {\n" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"test\",\n" +
                "    \"api_key\": \"test-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  }\n" +
                "}";



        ClusterProfile newClusterProperties = new ClusterProfile(
                "https://localhost:8154/go",
                "20m",
                "prod",
                "prod-api-key",
                "https://aws.api.com/api",
                null
        );

        ClusterProfile oldClusterProperties = new ClusterProfile(
                "https://localhost:8154/go",
                "20m",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        ClusterProfileChangedRequest request = ClusterProfileChangedRequest.fromJSON(json);
        assertThat(request.changeStatus(), equalTo(ChangeStatus.UPDATED));
        assertThat(request.oldClusterProperties(), Matchers.equalTo(oldClusterProperties));
        assertThat(request.clusterProperties(), Matchers.equalTo(newClusterProperties));
    }
}
