/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.gradle.ext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class AppRunExtension {

    private int appId = -1;
    private String serviceTierId;
    private String appName;
    private String vpcId;
    private String attachPublicIP = "false";
    private String subnetId;
   //Job service tier id
    private String jobSTId;
    private Map<String, String> envPairs = new HashMap<>();

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getServiceTierId() {
        return serviceTierId;
    }

    public void setServiceTierId(String serviceTierId) {
        this.serviceTierId = serviceTierId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getAttachPublicIP() {
        return attachPublicIP;
    }

    public void setAttachPublicIP(String attachPublicIP) {
        this.attachPublicIP = attachPublicIP;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getJobSTId() {
        return jobSTId;
    }

    public void setJobSTId(String jobSTId) {
        this.jobSTId = jobSTId;
    }

    public Map<String, String> getEnvPairs() {
        return envPairs;
    }

    public void setEnvPairs(Map<String, String> envPairs) {
        this.envPairs = envPairs;
    }

   

}
