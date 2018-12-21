package com.example.elasticagent;

import com.example.elasticagent.executors.GetProfileMetadataExecutor;
import com.example.elasticagent.executors.Metadata;
import com.example.elasticagent.requests.CreateAgentRequest;
import com.thoughtworks.go.plugin.api.logging.Logger;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.ResourceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesMonitoringEnabled;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.TagSpecification;

public class AWSInstanceBuilder {
	//TODO: the ec2 object should probably be a singleton
	private static Ec2Client ec2 = null;
	public static final Logger LOG = Logger.getLoggerFor(AWSInstanceBuilder.class);
	public static Clock clock = Clock.DEFAULT;
	
	private RunInstancesRequest.Builder runInstancesRequestBuilder;
	private CreateAgentRequest request = null;
	
	public AWSInstanceBuilder()
	{
		if(ec2 == null) {
			ec2 = Ec2Client.create();
		}
		
		runInstancesRequestBuilder = RunInstancesRequest.builder();
	}
	
	public AWSInstanceBuilder awsImageID(String id)
	{
		runInstancesRequestBuilder.imageId(id);
		return this;
	}
	
	public AWSInstanceBuilder awsInstanceType(String instanceType)
	{
		runInstancesRequestBuilder.instanceType(instanceType);
		return this;
	}
	
	public AWSInstanceBuilder awsSecurityGroupId(String id)
	{
		runInstancesRequestBuilder.securityGroupIds(id);
		return this;
	}
	
	public AWSInstanceBuilder awsKeyName(String keyName)
	{
		runInstancesRequestBuilder.keyName(keyName);
		return this;
	}
	
	public AWSInstanceBuilder createAgentRequest(CreateAgentRequest request)
	{
		this.request = request;
		
		//TODO: maybe push this functionality into request
		for(String key : this.request.properties().keySet())
		{
			Metadata field = GetProfileMetadataExecutor.getField(key);
			field.buildInstance(this, this.request.properties().get(key));
		}
		
		TagSpecification tagBuilder = TagSpecification.builder().tags(request.getTagsForInstance()).resourceType(ResourceType.INSTANCE).build();
		runInstancesRequestBuilder.tagSpecifications(tagBuilder);
		return this;
	}
	
	public RunInstancesRequest build(String userData) {
		RunInstancesRequest runInstancesRequest = runInstancesRequestBuilder				
    			.monitoring(RunInstancesMonitoringEnabled.builder().enabled(false).build())
    			.userData(userData)
    			.minCount(1)
    			.maxCount(1)
    			.build();
		return runInstancesRequest;
	}
}