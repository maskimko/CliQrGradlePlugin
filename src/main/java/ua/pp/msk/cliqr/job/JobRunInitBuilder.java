/*
 * Copyright 2016 Maksym Shkolnyi self project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.pp.msk.cliqr.job;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobRunInitBuilder {
  
    private JobRunInit ji;

    public JobRunInitBuilder(String serviceTierId, String name, String environment) {
        ji = new JobRunInit();
        ji.setServiceTierId(serviceTierId);
        ji.setName(name);
        ji.setEnvironment(environment);
    }
    
    
   

    public JobRunInitBuilder setServiceTierId(String serviceTierId) {
        ji.setServiceTierId(serviceTierId);
        return this;
    }


    public JobRunInitBuilder setName(String name) {
      ji.setName(name);
      return this;
    }

   

    public JobRunInitBuilder setEnvironment(String environment) {
       ji.setEnvironment(environment);
       return this;
    }

   
    public JobRunInitBuilder setParameters(Parameters parameters) {
        ji.setParameters(parameters);
        return this;
    }


    public JobRunInitBuilder setJobs(List<JobRun> jobs) {
        ji.setJobs(jobs);
        return this;
    }
    
    public JobRunInitBuilder addApplicationParameter(String name, String value){
       ji.addApplicationParameter(name, value);
        return this;
    }
    
    public JobRunInitBuilder setCloud(String cloud){
       ji.setCloud(cloud);
       return this;
    }
    
    public JobRunInitBuilder setCloudInstance(String instance){
        ji.setCloudInstance(instance);
        return this;
    }
    
    public JobRunInitBuilder setCloudProperties(Map<String, String> props){
        ji.setCloudProperties(props);
        return this;
    }
    
    public JobRunInitBuilder addCloudProperty(String name, String value){
        ji.addCloudProperty(name, value);
        return this;
        }
          
    public JobRunInit create(){
        return ji;
    }
    
}
