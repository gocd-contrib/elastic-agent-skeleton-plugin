package com.example.elasticagent;

import java.util.Collection;
import java.util.Map;

import com.example.elasticagent.executors.GetProfileMetadataExecutor;
import com.example.elasticagent.executors.AgentProfileField;
import com.example.elasticagent.requests.CreateAgentRequest;
import com.thoughtworks.go.plugin.api.logging.Logger;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.ResourceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesMonitoringEnabled;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.TagSpecification;



public class AWSInstanceBuilder {

	//TODO: the ec2 object should probably be a singleton
	private static Ec2Client ec2 = null;
	public static final Logger LOG = Logger.getLoggerFor(AWSInstanceBuilder.class);
	public static Clock clock = Clock.DEFAULT;
	
	private RunInstancesRequest.Builder runInstancesRequestBuilder;
	private TagSpecification.Builder instanceTagSpecificationBuilder;
	private CreateAgentRequest request = null;
	
	public AWSInstanceBuilder()
	{
		if(ec2 == null) {
			ec2 = Ec2Client.create();
		}
		
		runInstancesRequestBuilder = RunInstancesRequest.builder();
		instanceTagSpecificationBuilder = TagSpecification.builder().resourceType(ResourceType.INSTANCE);
	}
	
	public RunInstancesRequest.Builder getRunInstancesRequestBuilder()
	{
		return runInstancesRequestBuilder;
	}
	
	public AWSInstanceBuilder addInstanceTags(Collection<Tag> tags)
	{
		instanceTagSpecificationBuilder.tags(tags);
		return this;
	}
	
	public RunInstancesRequest build(String userData) {
		
		RunInstancesRequest runInstancesRequest = runInstancesRequestBuilder
				.tagSpecifications(instanceTagSpecificationBuilder.build())
    			.monitoring(RunInstancesMonitoringEnabled.builder().enabled(false).build())
    			.userData(userData)
    			.minCount(1)
    			.maxCount(1)
    			.build();
		return runInstancesRequest;
	}
}
