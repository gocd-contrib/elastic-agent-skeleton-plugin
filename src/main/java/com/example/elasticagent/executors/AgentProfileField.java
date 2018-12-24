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
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;



//TODO: why is there metadat and profileMetaData? is it JSON structure?
public class AgentProfileField {

	//TODO: name and refactor this
	@FunctionalInterface
	interface CommandDefinition 
	{ 
		AWSInstanceBuilder apply(AWSInstanceBuilder builder, String value); 
	}
	
	@FunctionalInterface
	public interface Command 
	{ 
		AWSInstanceBuilder apply(AWSInstanceBuilder builder); 
	}
	
    @Expose
    @SerializedName("key")
    private String key;

    @Expose
    @SerializedName("metadata")
    private ProfileMetadata metadata;
    
    //TODO: name this appropriately 
    private CommandDefinition commandDefinition;

    public AgentProfileField(String key, boolean required, boolean secure, CommandDefinition commandDefinition) {
        this(key, new ProfileMetadata(required, secure), commandDefinition);
    }

    public AgentProfileField(String key, CommandDefinition commandDefinition) {
        this(key, new ProfileMetadata(false, false), commandDefinition);
    }

    public AgentProfileField(String key, ProfileMetadata metadata, CommandDefinition commandDefinition) {
        this.key = key;
        this.metadata = metadata;
        this.commandDefinition = commandDefinition;
    }

    public Map<String, String> validate(String input) {
        HashMap<String, String> result = new HashMap<>();
        String validationError = doValidate(input);
        if (isNotBlank(validationError)) {
            result.put("key", key);
            result.put("message", validationError);
        }
        return result;
    }

    protected String doValidate(String input) {
        if (isRequired()) {
            if (StringUtils.isBlank(input)) {
                return this.key + " must not be blank.";
            }
        }
        return null;
    }


    public String getKey() {
        return key;
    }

    public boolean isRequired() {
        return metadata.required;
    }
    
    public AWSInstanceBuilder buildInstance(AWSInstanceBuilder builder, String value){
    	return commandDefinition.apply(builder, value);
    }
    
    public Command getCommand(String value)
    {
    	return (AWSInstanceBuilder builder) -> { return commandDefinition.apply(builder, value); };
    }

    public static class ProfileMetadata {
        @Expose
        @SerializedName("required")
        private Boolean required;

        @Expose
        @SerializedName("secure")
        private Boolean secure;

        public ProfileMetadata(boolean required, boolean secure) {
            this.required = required;
            this.secure = secure;
        }
    }
}