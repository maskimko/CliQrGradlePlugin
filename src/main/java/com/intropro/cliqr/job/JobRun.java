/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.intropro.cliqr.job;

import java.util.Map;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobRun {

    private String serviceTierId;
    private final Parameters parameters;

    public JobRun(String serviceTierId) {
        this.serviceTierId = serviceTierId;
        parameters = new Parameters();
    }

    
    
    public String getServiceTierId() {
        return serviceTierId;
    }

    public void setServiceTierId(String serviceTierId) {
        this.serviceTierId = serviceTierId;
    }

    public void setCloud(String cloud){
        getParameters().getCloudParameters().setCloud(cloud);
    }
    
    public void setCloudProperties(Map<String, String> props){
        getParameters().getCloudParameters().setCloudProperties(props);
    }
    
    public void addCloudParameter(Map.Entry<String, String> nv){
        addCloudParameter(nv.getKey(), nv.getValue());
    }
    public void addCloudParameter(String name, String value){
        getParameters().getCloudParameters().getCloudProperties().put(name, value);
    }
    
    public void setInstance(String instance){
        
    }

    public Parameters getParameters() {
        return parameters;
    }
    
    
}
