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
package ua.pp.msk.gradle.ext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class AppRunExtension {

    private int appId = -1;
    private String environment = null;
    private String instanceSize = null;
    private String appName = null;
    private String serviceTierId = appName + "-" + appId;
    private String version = "1.0";
    private String network = null;
    private String cloud = null;
    private boolean publicIp = false;
    private Map<String, String> params = new HashMap<>();
private int wait = -1;
private String errorMessage;
private String output;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
    

    public String getCloud() {
        return cloud;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getInstanceSize() {
        return instanceSize;
    }

    public void setInstanceSize(String instanceSize) {
        this.instanceSize = instanceSize;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

      public String getNetwork() {
        return network;
    }

    public void setNetwork(String sId) {
        this.network = sId;
    }

    public boolean isPublicIp() {
        return publicIp;
    }

    public void setPublicIp(boolean publicIp) {
        this.publicIp = publicIp;
    }
/**
 * Default value -1 means not to wait until job starting is finished
 * @return Job result waiting timeout in seconds
 */
    public int getWait() {
        return wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
      

}
