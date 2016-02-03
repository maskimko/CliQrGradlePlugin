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
public class RunTaskExtension {

    private int jobId = -1;
    private Map<String, String> envPairs = new HashMap<>();

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
