/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.intropro.cliqr.job;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
