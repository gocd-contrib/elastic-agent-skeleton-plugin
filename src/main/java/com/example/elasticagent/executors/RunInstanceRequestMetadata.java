package com.example.elasticagent.executors;

import com.example.elasticagent.AWSInstanceBuilder;

public class RunInstanceRequestMetadata extends Metadata {
	//TODO: names in this file are confusing
	public RunInstanceRequestMetadata(String key, boolean required, boolean secure, RunInstanceRequestBuilderInterface runInstanceRequestBuilderInterface) {
		super(key, required, secure, (AWSInstanceBuilder builder, String value) -> {return builder.RunInstancesRequestBuilder(runInstanceRequestBuilderInterface, value);});
	}

}
