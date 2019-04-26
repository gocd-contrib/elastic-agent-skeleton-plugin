package com.example.elasticagent.executors;

import com.example.elasticagent.*;
import com.example.elasticagent.models.AgentStatusReport;
import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.requests.AgentStatusReportRequest;
import com.example.elasticagent.views.ViewBuilder;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Date;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgentPluginStatusReportExecutorTest {
    @Mock
    private AgentInstances<ExampleInstance> agentInstances;
    @Mock
    private ViewBuilder viewBuilder;

    @Test
    public void shouldGetAgentStatusReportWithElasticAgentId() throws Exception {
        String agentId = "elastic-agent-id";
        AgentStatusReportRequest request = new AgentStatusReportRequest(agentId, null, new ClusterProfileProperties());
        AgentStatusReport agentStatusReport = new AgentStatusReport(null, agentId, null);

        ExampleInstance agentInstance = new ExampleInstance("name", new Date(), new HashMap<>(), null, new JobIdentifier());
        when(agentInstances.find(agentId)).thenReturn(agentInstance);
        when(agentInstances.getAgentStatusReport(request.clusterProperties(), agentInstance)).thenReturn(agentStatusReport);
        when(viewBuilder.build("status-report-template", agentStatusReport)).thenReturn("agentStatusReportView");

        GoPluginApiResponse goPluginApiResponse = new AgentStatusReportExecutor(request, agentInstances, viewBuilder)
                .execute();

        JsonObject expectedResponseBody = new JsonObject();
        expectedResponseBody.addProperty("view", "agentStatusReportView");
        assertThat(goPluginApiResponse.responseCode(), is(200));
        JSONAssert.assertEquals(expectedResponseBody.toString(), goPluginApiResponse.responseBody(), true);
    }

    @Test
    public void shouldGetAgentStatusReportWithJobIdentifier() throws Exception {
        JobIdentifier jobIdentifier = new JobIdentifier("up42", 2L, "label", "stage1", "1", "job", 1L);
        AgentStatusReportRequest request = new AgentStatusReportRequest(null, jobIdentifier, new ClusterProfileProperties());
        AgentStatusReport agentStatusReport = new AgentStatusReport(jobIdentifier, "elastic-agent-id", null);
        ExampleInstance instance = new ExampleInstance("name", new Date(), new HashMap<>(), null, new JobIdentifier());

        when(agentInstances.find(jobIdentifier)).thenReturn(instance);
        when(agentInstances.getAgentStatusReport(request.clusterProperties(), instance)).thenReturn(agentStatusReport);
        when(viewBuilder.build("status-report-template", agentStatusReport)).thenReturn("agentStatusReportView");

        GoPluginApiResponse goPluginApiResponse = new AgentStatusReportExecutor(request, agentInstances, viewBuilder)
                .execute();

        JsonObject expectedResponseBody = new JsonObject();
        expectedResponseBody.addProperty("view", "agentStatusReportView");
        assertThat(goPluginApiResponse.responseCode(), is(200));
        JSONAssert.assertEquals(expectedResponseBody.toString(), goPluginApiResponse.responseBody(), true);
    }

    @Test
    public void shouldRenderContainerNotFoundAgentStatusReportViewWhenNoContainerIsRunningForProvidedJobIdentifier() throws Exception {
        JobIdentifier jobIdentifier = new JobIdentifier("up42", 2L, "label", "stage1", "1", "job", 1L);

        AgentStatusReportRequest agentStatusReportRequest = new AgentStatusReportRequest(null, jobIdentifier, null);

        when(agentInstances.find(jobIdentifier)).thenReturn(null);
        when(viewBuilder.build("not-running-template")).thenReturn("errorView");

        GoPluginApiResponse goPluginApiResponse = new AgentStatusReportExecutor(agentStatusReportRequest, agentInstances, viewBuilder)
                .execute();

        JsonObject expectedResponseBody = new JsonObject();
        expectedResponseBody.addProperty("view", "errorView");
        assertThat(goPluginApiResponse.responseCode(), is(200));
        JSONAssert.assertEquals(expectedResponseBody.toString(), goPluginApiResponse.responseBody(), true);
    }

    @Test
    public void shouldRenderContainerNotFoundAgentStatusReportViewWhenNoContainerIsRunningForProvidedElasticAgentId() throws Exception {
        String elasticAgentId = "elastic-agent-id";
        AgentStatusReportRequest agentStatusReportRequest = new AgentStatusReportRequest(elasticAgentId, null, null);

        when(agentInstances.find(elasticAgentId)).thenReturn(null);
        when(viewBuilder.build("not-running-template")).thenReturn("errorView");

        GoPluginApiResponse goPluginApiResponse = new AgentStatusReportExecutor(agentStatusReportRequest, agentInstances, viewBuilder)
                .execute();

        JsonObject expectedResponseBody = new JsonObject();
        expectedResponseBody.addProperty("view", "errorView");
        assertThat(goPluginApiResponse.responseCode(), is(200));
        JSONAssert.assertEquals(expectedResponseBody.toString(), goPluginApiResponse.responseBody(), true);
    }
}
