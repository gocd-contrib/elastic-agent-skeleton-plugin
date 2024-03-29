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

import com.example.elasticagent.requests.ValidateElasticAgentProfileRequest;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Collections;

public class ValidateElasticAgentProfileRequestExecutorTest {
    @Test
    public void shouldBarfWhenUnknownKeysArePassed() throws Exception {
        ValidateElasticAgentProfileRequestExecutor executor = new ValidateElasticAgentProfileRequestExecutor(new ValidateElasticAgentProfileRequest(Collections.singletonMap("foo", "bar")));
        String json = executor.execute().responseBody();
        JSONAssert.assertEquals("[{\"message\":\"Image must not be blank\",\"key\":\"Image\"},{\"message\":\"Invalid size: null\",\"key\":\"MaxMemory\"},{\"key\":\"foo\",\"message\":\"Is an unknown property\"}]", json, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    public void shouldValidateMandatoryKeys() throws Exception {
        ValidateElasticAgentProfileRequestExecutor executor = new ValidateElasticAgentProfileRequestExecutor(new ValidateElasticAgentProfileRequest(Collections.<String, String>emptyMap()));
        String json = executor.execute().responseBody();
        JSONAssert.assertEquals("[{\"message\":\"Image must not be blank\",\"key\":\"Image\"},{\"message\":\"Invalid size: null\",\"key\":\"MaxMemory\"}]", json, JSONCompareMode.NON_EXTENSIBLE);
    }
}