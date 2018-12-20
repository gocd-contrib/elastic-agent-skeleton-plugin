/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.elasticagent.requests;

import com.example.elasticagent.AgentInstances;
import com.example.elasticagent.Constants;
import com.example.elasticagent.PluginRequest;
import com.example.elasticagent.RequestExecutor;
import com.example.elasticagent.executors.CreateAgentRequestExecutor;
import com.example.elasticagent.models.JobIdentifier;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import software.amazon.awssdk.services.ec2.model.Tag;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class CreateAgentRequest {
	private static final String AGENT_AUTO_REGISTER_KEY = "agent.auto.register.key";
	private static final String AGENT_AUTO_REGISTER_ENVIRONMENTS = "agent.auto.register.environments";
	private static final String AGENT_AUTO_REGISTER_ELASTIC_AGENT_ID = "agent.auto.register.elasticAgent.agentId";
	private static final String AGENT_AUTO_REGISTER_ELASTIC_AGENT_PLUGIN_ID = "agent.auto.register.elasticAgent.pluginId";
	private static final String AGENT_AUTO_REGISTER_HOSTNAME = "agent.auto.register.hostname";
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    private String autoRegisterKey;
    private Map<String, String> properties;
    private String environment;
    private JobIdentifier jobIdentifier;


    public CreateAgentRequest() {
    }

    public CreateAgentRequest(String autoRegisterKey, Map<String, String> properties, String environment, JobIdentifier jobIdentifier) {
        this.autoRegisterKey = autoRegisterKey;
        this.properties = properties;
        this.environment = environment;
        this.jobIdentifier = jobIdentifier;
    }

    public String autoRegisterKey() {
        return autoRegisterKey;
    }

    public Map<String, String> properties() {
        return properties;
    }

    public String environment() {
        return environment;
    }

    public JobIdentifier jobIdentifier() {
        return jobIdentifier;
    }
    
    public String toJson() {
        return GSON.toJson(this);
    }

    public static CreateAgentRequest fromJSON(String json) {
        return GSON.fromJson(json, CreateAgentRequest.class);
    }

    public RequestExecutor executor(AgentInstances agentInstances, PluginRequest pluginRequest) {
        return new CreateAgentRequestExecutor(this, agentInstances, pluginRequest);
    }

    //TODO: use this method instead of what i wrote before
    public Properties autoregisterProperties() {
        Properties properties = new Properties();

        if (StringUtils.isNotBlank(autoRegisterKey)) {
            properties.put(AGENT_AUTO_REGISTER_KEY, autoRegisterKey);
        }

        if (StringUtils.isNotBlank(environment)) {
            properties.put(AGENT_AUTO_REGISTER_ENVIRONMENTS, environment);
        }
        
        properties.put(AGENT_AUTO_REGISTER_ELASTIC_AGENT_PLUGIN_ID, Constants.PLUGIN_ID);

        return properties;
    }
    
    public String autoregisterPropertiesAsString(String elasticAgentId, String elasticAgentHostName) {
    	Properties properties = autoregisterProperties();
    	
        if (StringUtils.isNotBlank(elasticAgentId)) {
        	properties.put(AGENT_AUTO_REGISTER_ELASTIC_AGENT_ID, elasticAgentId);
        }
        
        if (StringUtils.isNotBlank(elasticAgentHostName)) {
        	properties.put(AGENT_AUTO_REGISTER_HOSTNAME, elasticAgentHostName);
        }
    	
    	StringBuilder stringBuilder = new StringBuilder();
    	properties.keySet().forEach(key -> {
    		//TODO: move this string append to a util function because of its specificity to powershell
    		stringBuilder.append(key.toString() + "=" + properties.getProperty(key.toString()) + "\r\n");
    	});
    	return stringBuilder.toString();
    }
    
    public Collection<Tag> getTagsForInstance()
    {
    	ArrayList<Tag> tags = new ArrayList<Tag>();
    	
    	Properties properties = autoregisterProperties();
    	properties.keySet().forEach(key -> {
    		tags.add(Tag.builder().key(key.toString()).value(properties.getProperty(key.toString())).build());
    	});
    	
    	tags.addAll(this.jobIdentifier.getTagsForInstance());
    	return tags;
    }

}
