/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
