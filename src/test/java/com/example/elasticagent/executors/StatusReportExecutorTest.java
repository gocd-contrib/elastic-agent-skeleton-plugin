package com.example.elasticagent.executors;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.ExampleInstance;
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.PluginSettings;
import com.example.elasticagent.models.StatusReport;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatusReportExecutorTest {

    @Mock
    private PluginRequest pluginRequest;

    @Mock
    private PluginSettings pluginSettings;

    @Mock
    private ViewBuilder viewBuilder;

    @Mock
    private AgentInstances<ExampleInstance> agentInstances;

    @Test
    public void shouldGetStatusReport() throws Exception {
        StatusReport statusReport = new StatusReport("0.1.2");
        when(pluginRequest.getPluginSettings()).thenReturn(pluginSettings);
        when(agentInstances.getStatusReport(pluginSettings)).thenReturn(statusReport);
        when(viewBuilder.build("status-report-template", statusReport)).thenReturn("statusReportView");
        StatusReportExecutor statusReportExecutor = new StatusReportExecutor(pluginRequest, agentInstances, viewBuilder);

        GoPluginApiResponse goPluginApiResponse = statusReportExecutor.execute();

        JsonObject expectedResponseBody = new JsonObject();
        expectedResponseBody.addProperty("view", "statusReportView");
        assertThat(goPluginApiResponse.responseCode(), is(200));
        JSONAssert.assertEquals(expectedResponseBody.toString(), goPluginApiResponse.responseBody(), true);
    }
}
