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
package ua.pp.msk.cliqr;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * @author maskimko
 */
public class Environment {
   // PI,EE,AWS_PI,AWS_EE,MSDC_PI,MSDC_EE,MSDC_SBPI,MSDC_SBEE
    private static Environment env = null;
    private Map<String, String> environments;
    private final Enumeration<String> envNames;
    /**
     * Environment resource bundle name
     */
    private static final String ERB = "environments.properties";
    
    public static Environment getEnvironment(){
        if (env == null ) {
            synchronized (Environment.class) {
            if(env == null) {
                env = new Environment();
            }
        }
        }
        return env;
    }
    
    private Environment(){
        ResourceBundle erb = ResourceBundle.getBundle(ERB);
        envNames  = erb.getKeys();
        while(env.envNames.hasMoreElements()){
            String nextElement = envNames.nextElement();
            environments.put(nextElement, erb.getObject(nextElement).toString());
        }
    }
    
    /**
     * Get environment description
     * @param name Environment name
     * @return Environment description
     */
    public String getDescription(String name) {
        if (environments.containsKey(name)){
            return environments.get(name);
        }
        return null;
    }

    public Enumeration<String> getEnvironments() {
        return envNames;
    }
    
    public boolean contains(String envName){
        return environments.containsKey(envName);
    }
}
