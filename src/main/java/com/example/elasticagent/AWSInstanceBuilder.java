package com.example.elasticagent;

import java.util.Map;

import com.example.elasticagent.executors.GetProfileMetadataExecutor;
import com.example.elasticagent.executors.Metadata;
import com.example.elasticagent.executors.RunInstanceRequestBuilderInterface;
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
	
	public AWSInstanceBuilder RunInstancesRequestBuilder(RunInstanceRequestBuilderInterface builder, String value)
	{
		this.runInstancesRequestBuilder = builder.buildRequest(this.runInstancesRequestBuilder, value);
		return this;
	}
	
	public AWSInstanceBuilder applyField(Metadata field, String value)
	{
		field.buildInstance(this, value);
		return this;
	}
	
	public AWSInstanceBuilder createAgentRequest(CreateAgentRequest request)
	{
		this.request = request;
		
		//TODO: maybe push this functionality into request
		this.request.forEachProperty((Metadata field, String value) -> {
			field.buildInstance(this, value);
		});
		
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
