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

import java.io.StringWriter;
import java.util.List;

import com.example.elasticagent.RequestExecutor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class GetProfileViewExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    
    @Override
    public GoPluginApiResponse execute() throws Exception {
    	class Fields
    	{
    		public List<Metadata> fields = GetProfileMetadataExecutor.FIELDS;
    		public String openCurly = "{";
    		public String closeCurly = "}";
    	}
    	
    	MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("profile.mustache");
        StringWriter templateWriter = new StringWriter();
        mustache.execute(templateWriter, new Fields());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("template", templateWriter.toString());
        DefaultGoPluginApiResponse defaultGoPluginApiResponse = new DefaultGoPluginApiResponse(200, GSON.toJson(jsonObject));
        return defaultGoPluginApiResponse;
    }

}
