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

import com.example.elasticagent.utils.Util;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetPluginSettingsIconExecutorTest {

    @Test
    public void rendersIconInBase64() throws Exception {
        GoPluginApiResponse response = new GetPluginSettingsIconExecutor().execute();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        HashMap<String, String> hashMap = new Gson().fromJson(response.responseBody(),
                type);
        assertThat(hashMap.size(), is(2));
        assertThat(hashMap.get("content_type"), is("image/svg+xml"));
        System.out.println("hashMap = " + hashMap.get("data"));
        assertThat(Util.readResourceBytes("/plugin-icon.svg"), is(BaseEncoding.base64().decode(hashMap.get("data"))));
    }
}