package com.example.elasticagent.executors;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class GetCapabilitiesExecutorTest {

    @Test
    public void shouldReturnResponse() throws Exception {
        GoPluginApiResponse response = new GetCapabilitiesExecutor().execute();

        assertThat(response.responseCode(), is(200));
        JSONObject expected = new JSONObject()
                .put("supports_plugin_status_report", true)
                .put("supports_agent_status_report", true)
                .put("supports_cluster_status_report", true);
        assertEquals(expected, new JSONObject(response.responseBody()), true);
    }
}
