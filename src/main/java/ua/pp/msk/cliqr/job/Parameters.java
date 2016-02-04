/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.pp.msk.cliqr.job;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
 public class Parameters {
     private CloudParameters cloudParameters;
     private Map<String, String> appParams;

     Parameters(){
         cloudParameters = new CloudParameters();
         appParams = new LinkedHashMap<>();
     }
     
    public CloudParameters getCloudParameters() {
        return cloudParameters;
    }

    public void setCloudParameters(CloudParameters cloudParameters) {
        this.cloudParameters = cloudParameters;
    }

    public Map<String, String> getAppParams() {
        return appParams;
    }

    public void setAppParams(Map<String, String> appParams) {
        this.appParams = appParams;
    }

   
     
     
}
