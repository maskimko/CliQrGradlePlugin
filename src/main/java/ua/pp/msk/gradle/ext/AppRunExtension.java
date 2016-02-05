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

    private String vpcId = null;
    private String sId = null;
    private String cloud = null;
    private boolean publicIp = false;

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    private Map<String, String> params = new HashMap<>();

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

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getNetwork() {
        return sId;
    }

    public void setNetwork(String sId) {
        this.sId = sId;
    }

    public boolean isPublicIp() {
        return publicIp;
    }

    public void setPublicIp(boolean publicIp) {
        this.publicIp = publicIp;
    }

}
