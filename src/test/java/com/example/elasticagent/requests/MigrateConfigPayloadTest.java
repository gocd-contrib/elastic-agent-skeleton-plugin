package com.example.elasticagent.requests;

import com.example.elasticagent.ClusterProfile;
import com.example.elasticagent.PluginSettings;
import org.junit.jupiter.api.Test;

import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MigrateConfigPayloadTest {

    @Test
    public void shouldDeserializeRequest() {
        String json = "{" +
                "    \"plugin_settings\":{}," +
                "    \"cluster_profiles\":[]," +
                "    \"elastic_agent_profiles\":[]" +
                "}\n";

        MigrateConfigPayload request = MigrateConfigPayload.fromJSON(json);

        assertThat(request.clusterProfiles().size(), is(0));
        assertThat(request.elasticAgentProfileProperties().size(), is(0));
        assertThat(request.pluginSettings(), is(new PluginSettings()));
    }

    @Test
    public void shouldDesesrializeRequest() {
        String json = "{" +
                "  \"plugin_settings\":{" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"test\",\n" +
                "    \"api_key\": \"test-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  }," +
                "  \"cluster_profiles\": [{\n" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"test\",\n" +
                "    \"api_key\": \"test-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  }],\n" +
                "  \"elastic_agent_profiles\":[{" +
                "    \"Image\": \"alpine:latest\"\n" +
                "  }]\n" +
                "}\n";

        ClusterProfile expectedClusterProfile = new ClusterProfile(
                "https://localhost:8154/go",
                "20m",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        PluginSettings expectedSettings = new PluginSettings(
                "https://localhost:8154/go",
                "20m",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        MigrateConfigPayload request = MigrateConfigPayload.fromJSON(json);

        assertThat(request.clusterProfiles().size(), is(1));
        assertThat(request.clusterProfiles().get(0), is(expectedClusterProfile));
        assertThat(request.pluginSettings(), is(expectedSettings));
        assertThat(request.elasticAgentProfileProperties().size(), is(1));
        assertThat(request.elasticAgentProfileProperties().get(0), is(singletonMap("Image", "alpine:latest")));
    }
}