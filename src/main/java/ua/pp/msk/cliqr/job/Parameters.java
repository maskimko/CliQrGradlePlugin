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
