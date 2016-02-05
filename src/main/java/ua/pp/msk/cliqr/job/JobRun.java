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

package ua.pp.msk.cliqr.job;

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
