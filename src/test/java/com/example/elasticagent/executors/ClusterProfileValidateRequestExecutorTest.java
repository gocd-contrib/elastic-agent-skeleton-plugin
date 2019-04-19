package com.example.elasticagent.executors;

import com.example.elasticagent.requests.ClusterProfileValidateRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ClusterProfileValidateRequestExecutorTest {
    @Test
    public void shouldValidateABadConfiguration() throws Exception {
        ClusterProfileValidateRequest request = new ClusterProfileValidateRequest(new HashMap<>());
        GoPluginApiResponse response = new ClusterProfileValidateRequestExecutor(request).execute();

        assertThat(response.responseCode(), is(200));
        String expectedJSON = "[\n" +
                "  {\n" +
                "    \"message\": \"Go Server URL must not be blank.\",\n" +
                "    \"key\": \"go_server_url\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Agent auto-register Timeout (in minutes) must be a positive integer.\",\n" +
                "    \"key\": \"auto_register_timeout\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"API URL must not be blank.\",\n" +
                "    \"key\": \"api_url\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"API User must not be blank.\",\n" +
                "    \"key\": \"api_user\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"API Key must not be blank.\",\n" +
                "    \"key\": \"api_key\"\n" +
                "  }\n" +
                "]";
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), false);
    }

    @Test
    public void shouldValidateAGoodConfiguration() throws Exception {
        Map<String, String> profileProperties = new HashMap<>();
        profileProperties.put("api_url", "https://api.example.com");
        profileProperties.put("api_user", "bob");
        profileProperties.put("api_key", "p@ssw0rd");
        profileProperties.put("go_server_url", "https://ci.example.com");
        profileProperties.put("auto_register_timeout", "10");

        ClusterProfileValidateRequest request = new ClusterProfileValidateRequest(profileProperties);
        GoPluginApiResponse response = new ClusterProfileValidateRequestExecutor(request).execute();

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals("[]", response.responseBody(), true);
    }
}