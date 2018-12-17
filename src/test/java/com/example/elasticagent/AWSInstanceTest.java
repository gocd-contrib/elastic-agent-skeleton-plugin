package com.example.elasticagent;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

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
        String json = "{\n" +
                "  \"auto_register_key\": \"secret-key\",\n" +
                "  \"properties\": {\n" +
                "    \"key1\": \"value1\",\n" +
                "    \"key2\": \"value2\"\n" +
                "  },\n" +
                "  \"environment\": \"test,environment\"\n" +
                "}";

        CreateAgentRequest request = CreateAgentRequest.fromJSON(json);
        
        PluginSettings pluginSettings = PluginSettings.fromJSON("{" +
                "\"api_user\": \"bob\", " +
                "\"api_key\": \"p@ssw0rd\", " +
                "\"api_url\": \"https://cloud.example.com/api/v1\" " +
                "}");
        
        AWSInstance newInstance = AWSInstance.Factory(request, pluginSettings);
    }
	
	
}
