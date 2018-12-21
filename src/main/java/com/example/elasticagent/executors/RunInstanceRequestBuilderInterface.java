package com.example.elasticagent.executors;

import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;

//TODO: name and refactor this
@FunctionalInterface
public interface RunInstanceRequestBuilderInterface 
{ 
	RunInstancesRequest.Builder buildRequest(RunInstancesRequest.Builder builder, String value); 
}
