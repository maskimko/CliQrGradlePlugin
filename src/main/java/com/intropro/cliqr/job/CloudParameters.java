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
public class CloudParameters {

    private String cloud;
    private Map<String, String> cloudProperties;
    private String instance;

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public Map<String, String> getCloudProperties() {
        return cloudProperties;
    }

    public void setCloudProperties(Map<String, String> cloudProperties) {
        this.cloudProperties = cloudProperties;
    }

   

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
    
    
}
