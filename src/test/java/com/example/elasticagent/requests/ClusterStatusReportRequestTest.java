package com.example.elasticagent.requests;

import com.example.elasticagent.ClusterProfileProperties;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ClusterStatusReportRequestTest {

    @Test
    public void shouldDeserializeFromJSON() {
        String json = "{\n" +
                "  \"cluster_profile_properties\": {\n" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"test\",\n" +
                "    \"api_key\": \"test-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  }\n" +
                "}";

        ClusterStatusReportRequest clusterStatusReportRequest = ClusterStatusReportRequest.fromJSON(json);

        ClusterProfileProperties expectedClusterProfile = new ClusterProfileProperties(
                "https://localhost:8154/go",
                "20m",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        assertThat(clusterStatusReportRequest.getClusterProfile(), is(expectedClusterProfile));
    }
}