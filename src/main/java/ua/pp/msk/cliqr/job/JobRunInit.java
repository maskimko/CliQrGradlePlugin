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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobRunInit {

    /**
     * Name of CliQr application + application Id
     */
    private String serviceTierId;
    /**
     * Application Run name
     */
    private String name;
    /**
     * Environment where to run application 
     */
    private String environment;
    /**
     * Start application run with parameters
     */
    private Parameters parameters;
    
    private List<JobRun> jobs;

    public String getServiceTierId() {
        return serviceTierId;
    }

    public void setServiceTierId(String serviceTierId) {
        this.serviceTierId = serviceTierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public List<JobRun> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobRun> jobs) {
        this.jobs = jobs;
    }
    
    public void addApplicationParameter(String name, String value){
        getParameters().getAppParams().put(name, value);
    }
    
    public void setCloud(String cloud){
        getParameters().getCloudParameters().setCloud(cloud);
    }
    
    public void setCloudInstance(String instance){
        getParameters().getCloudParameters().setInstance(instance);
    }
    
    public void setCloudProperties(Map<String, String> props){
        getParameters().getCloudParameters().setCloudProperties(props);
    }
    
    public void addCloudProperty(String name, String value){
        getParameters().getCloudParameters().getCloudProperties().put(name, value);
    }
          
    public String toJson(){
        GsonBuilder gb = new GsonBuilder();
        gb.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        gb.setPrettyPrinting();
        Gson gson = gb.create();
        String toJson = gson.toJson(this);
        return toJson;
    }
}
