/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intropro.cliqr;

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
