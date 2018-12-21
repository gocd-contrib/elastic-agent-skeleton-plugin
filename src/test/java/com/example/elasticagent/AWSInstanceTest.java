package com.example.elasticagent;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.example.elasticagent.models.JobIdentifier;
import com.example.elasticagent.requests.CreateAgentRequest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AWSInstanceTest {

    @Test
    public void AWSInstanceFactory() throws Exception {
    	
    	//Here is some json for testing
    	//"{\"auto_register_key\":\"4f6482d7-9a7c-4ced-9b4d-694ee9f345c2\",\"properties\":{\"ImageId\":\"ami-017bf00eb0d4c7182\",\"InstanceType\":\"t2.micro\",\"SecurityGroupId\":\"sg-00a22b0befc186b4c\",\"KeyName\":\"MyFirstKey.pem\"},\"job_identifier\":{\"pipeline_name\":\"CleanUpAWS\",\"pipeline_counter\":23,\"pipeline_label\":\"23\",\"stage_name\":\"defaultStage\",\"stage_counter\":\"1\",\"job_name\":\"defaultJob\",\"job_id\":45}}"
        String json = "{\n" +
                "  \"auto_register_key\": \"secret-key\",\n" +
                "  \"properties\": {\n" +
                "    \"key1\": \"value1\",\n" +
                "    \"key2\": \"value2\"\n" +
                "  },\n" +
                "  \"environment\": \"test,environment\",\n" +
                "  \"job_identifier\": {\n" +
                "    \"pipeline_name\": \"ElasticPipe\",\n" +
                "    \"pipeline_counter\": 13,\n" +
                "    \"pipeline_label\": \"13\",\n" +
                "    \"stage_name\": \"defaultStage\",\n" +
                "    \"stage_counter\": \"1\",\n" +
                "    \"job_name\": \"defaultJob\",\n" +
                "    \"job_id\": 42\n" +
                "  }\n" +
                "}";

        CreateAgentRequest request = CreateAgentRequest.fromJSON(json);
        
        PluginSettings pluginSettings = PluginSettings.fromJSON("{" +
                "\"api_user\": \"bob\", " +
                "\"api_key\": \"p@ssw0rd\", " +
                "\"api_url\": \"https://cloud.example.com/api/v1\" " +
                "}");
        
        //TODO: set a way to run AWS and non-AWS test separately
        AWSInstance newInstance = AWSInstance.Factory(request, pluginSettings);
    }
	
	
}
