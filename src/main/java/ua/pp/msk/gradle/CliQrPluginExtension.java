/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.gradle;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class CliQrPluginExtension {

    private String host = null;
    private String user = null;
    private String apiKey = null;
    private int jobId = -1;
    private Map<String, String> envPairs = new HashMap<>();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public Map<String, String> getEnvPairs() {
        return envPairs;
    }

    public void setEnvPairs(Map<String, String> envPairs) {
        this.envPairs = envPairs;
    }
    
    
  

}
