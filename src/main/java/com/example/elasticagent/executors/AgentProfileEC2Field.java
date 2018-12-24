package com.example.elasticagent.executors;

import com.example.elasticagent.AWSInstanceBuilder;

import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;

public class AgentProfileEC2Field extends AgentProfileField {
	
	@FunctionalInterface
	interface CommandDefinition 
	{ 
		RunInstancesRequest.Builder apply(RunInstancesRequest.Builder builder, String value); 
	}
	
	//TODO: names in this file are confusing
	public AgentProfileEC2Field(String key, boolean required, boolean secure, CommandDefinition commandDefinition) {
		super(key, required, secure, (AWSInstanceBuilder instanceBuilder, String value) -> 
		{
			commandDefinition.apply(instanceBuilder.getRunInstancesRequestBuilder(), value);
			return instanceBuilder;
		});
	}
}
