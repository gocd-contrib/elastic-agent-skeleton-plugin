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

import com.example.elasticagent.AWSInstanceBuilder;
import com.example.elasticagent.RequestExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GetProfileMetadataExecutor implements RequestExecutor {
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    
    //TODO: how many steps does it take to add a field?  can the number of steps be reduced?
    public static final Metadata IMAGE_ID = new Metadata("ImageId", true, false, (AWSInstanceBuilder builder, String value) -> {return builder.awsImageID(value);});
    public static final Metadata INSTANCE_TYPE = new Metadata("InstanceType", true, false, (AWSInstanceBuilder builder, String value) -> {return builder.awsInstanceType(value);});
    public static final Metadata SECURITY_GROUP_ID = new Metadata("SecurityGroupId", true, false, (AWSInstanceBuilder builder, String value) -> {return builder.awsSecurityGroupId(value);});
    public static final Metadata KEY_NAME = new Metadata("KeyName", true, false, (AWSInstanceBuilder builder, String value) -> {return builder.awsKeyName(value);});

    public static final ConcurrentHashMap<String, Metadata> FIELDS = new ConcurrentHashMap<String, Metadata>();
    
    public static List<Metadata> getFields()
    {
    	return new ArrayList<Metadata>(FIELDS.values());
    }
    
    public static Metadata getField(String key)
    {
    	return FIELDS.get(key);
    }
    
    private static void addField(Metadata field)
    {
    	FIELDS.put(field.getKey(), field);
    }
    
    static {
    	addField(IMAGE_ID);
    	addField(INSTANCE_TYPE);
    	addField(SECURITY_GROUP_ID);
    	addField(KEY_NAME);
    }

    @Override

    public GoPluginApiResponse execute() throws Exception {
        return new DefaultGoPluginApiResponse(200, GSON.toJson(FIELDS));
    }
}
