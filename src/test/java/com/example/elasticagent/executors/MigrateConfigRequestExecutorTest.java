package com.example.elasticagent.executors;

import com.example.elasticagent.ClusterProfile;
import com.example.elasticagent.ClusterProfileProperties;
import com.example.elasticagent.PluginSettings;
import com.example.elasticagent.requests.MigrateConfigPayload;
import com.example.elasticagent.utils.Util;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class MigrateConfigRequestExecutorTest {
    @Test
    public void shouldMigratePluginSettingsToClusterProfile() throws Exception {
        ClusterProfile expectedDefaultCluster = new ClusterProfile("default", Util.pluginId(),
                new ClusterProfileProperties(
                        "https://localhost:8154/go",
                        "20",
                        "test",
                        "test-api-key",
                        "https://aws.api.com/api",
                        null
                ));

        PluginSettings pluginSettings = new PluginSettings(
                "https://localhost:8154/go",
                "20",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        Map<String, String> elasticProfile = new HashMap<>();
        elasticProfile.put("Image", "alpine:latest");

        List<ClusterProfile> clusterProfiles = new ArrayList<>();
        clusterProfiles.add(expectedDefaultCluster);

        GoPluginApiResponse goResponse = new MigrateConfigRequestExecutor(new MigrateConfigPayload(
                pluginSettings,
                asList(elasticProfile),
                new ArrayList<>()
        )).execute();

        MigrateConfigPayload response = MigrateConfigPayload.fromJSON(goResponse.responseBody());

        assertThat(goResponse.responseCode(), is(200));
        assertThat(response.clusterProfiles().size(), is(1));
        assertThat(response.clusterProfiles().get(0), is(expectedDefaultCluster));
        assertThat(response.elasticAgentProfileProperties().size(), is(1));
        assertThat(response.elasticAgentProfileProperties().get(0).keySet(), hasItem("clusterProfileId"));
        assertThat(response.elasticAgentProfileProperties().get(0).get("clusterProfileId"), is(expectedDefaultCluster.getId()));
    }
}