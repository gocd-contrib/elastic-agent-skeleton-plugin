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

package com.example.elasticagent.executors;

import com.example.elasticagent.RequestExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GetProfileMetadataExecutor implements RequestExecutor {
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private static final ConcurrentHashMap<String, AgentProfileField> FIELDS = new ConcurrentHashMap<String, AgentProfileField>();
    private static final Logger LOG = Logger.getLoggerFor(GetProfileMetadataExecutor.class);
    
    public static List<AgentProfileField> getFields()
    {
    	return new ArrayList<AgentProfileField>(FIELDS.values());
    }
    
    public static AgentProfileField.Command getField(String key, String value) throws Exception
    {
    	if(FIELDS.containsKey(key))
    		return FIELDS.get(key).getCommand(value);
    	else
    		throw new Exception("Field Name \"" + key + "\" cannot be found in the elastic agent profile metadata.");
    }
    
    private static void addField(AgentProfileField field)
    {
    	FIELDS.put(field.getKey(), field);
    }
    
    static {
        //TODO: how many steps does it take to add a field?  can the number of steps be reduced?
        addField(new AgentProfileEC2Field("ImageId", true, false, (Builder builder, String value) -> {return builder.imageId(value);}));
        addField(new AgentProfileEC2Field("InstanceType", true, false, (Builder builder, String value) -> {return builder.instanceType(value);}));
        addField(new AgentProfileEC2Field("SecurityGroupId", true, false, (Builder builder, String value) -> {return builder.securityGroupIds(value);}));
        addField(new AgentProfileEC2Field("KeyName", true, false, (Builder builder, String value) -> {return builder.keyName(value);}));
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        return new DefaultGoPluginApiResponse(200, GSON.toJson(GetProfileMetadataExecutor.getFields()));
    }
}
