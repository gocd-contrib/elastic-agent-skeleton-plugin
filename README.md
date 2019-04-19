# GoCD Elastic agent plugin skeleton

This is merely a skeleton plugin that plugin developers can fork to get quickly 
started with writing elastic agent plugins for GoCD.

All the documentation is hosted at https://plugin-api.gocd.io/current/elastic-agents/.


## Getting started

* Edit the file `build.gradle`
* Edit the file `settings.gradle`
* Edit the `GetClusterProfileMetadataExecutor.java` class to add any configuration fields that should be shown in the view.
* Edit the `cluster-profile.template.html` file which contains the view for the plugin settings page of your plugin.
* Edit the `ClusterProfile.java` file which contains the model for your cluster profile properties.
* Implement the `ExampleAgentInstances.java` class to get a really basic elastic agent plugin working.

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## License

```plain
Copyright 2017 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## About the license and releasing your plugin under a different license

The skeleton code in this repository is licensed under the Apache 2.0 license. The license itself specifies the terms
under which derivative works may be distributed (the license also defines derivative works). The Apache 2.0 license is a
permissive open source license that has minimal requirements for downstream licensors/licensees to comply with.

This does not prevent your plugin from being licensed under a different license as long as you comply with the relevant
clauses of the Apache 2.0 license (especially section 4). Typically, you clone this repository and keep the existing
copyright notices. You are free to add your own license and copyright notice to any modifications.

This is not legal advice. Please contact your lawyers if needed.
