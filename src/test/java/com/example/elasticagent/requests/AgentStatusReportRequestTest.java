package com.example.elasticagent.requests;

import com.example.elasticagent.ClusterProfile;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AgentStatusReportRequestTest {

    @Test
    public void shouldDeserializeFromJSON() {
        final String requestBody = "{\n" +
                "  \"job_identifier\": {\n" +
                "    \"pipeline_name\": \"up42\",\n" +
                "    \"pipeline_label\": \"Test\",\n" +
                "    \"pipeline_counter\": 2,\n" +
                "    \"stage_name\": \"up42_stage\",\n" +
                "    \"stage_counter\": \"10\",\n" +
                "    \"job_name\": \"up42_job\",\n" +
                "    \"job_id\": -1\n" +
                "  },\n" +
                "  \"cluster_profile_properties\":{" +
                "    \"go_server_url\": \"https://localhost:8154/go\",\n" +
                "    \"auto_register_timeout\": \"20m\",\n" +
                "    \"api_user\": \"test\",\n" +
                "    \"api_key\": \"test-api-key\",\n" +
                "    \"api_url\": \"https://aws.api.com/api\"\n" +
                "  }," +
                "  \"elastic_agent_id\": \"GoCD193659b3b930480287b898eeef0ade37\"\n" +
                "}";

        AgentStatusReportRequest agentStatusReportRequest = AgentStatusReportRequest.fromJSON(requestBody);

        ClusterProfile expectedClusterProperties = new ClusterProfile(
                "https://localhost:8154/go",
                "20m",
                "test",
                "test-api-key",
                "https://aws.api.com/api",
                null
        );

        assertThat(agentStatusReportRequest.getElasticAgentId(), is("GoCD193659b3b930480287b898eeef0ade37"));
        assertThat(agentStatusReportRequest.clusterProperties(), is(expectedClusterProperties));
        assertThat(agentStatusReportRequest.getJobIdentifier().represent(), is("up42/2/up42_stage/10/up42_job"));
    }

}
