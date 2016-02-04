/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.intropro.cliqr.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobFilter {
    private final String key;
    private final String value;
    
    public JobFilter(String key, String value){
        this.key = key;
        this.value = value;
    }
    
    
    //FIXME Very bad implementation!!!1 Fix in the future!!!
    
    
    public ParsedJob[] filter(ParsedJob[] pjs){
        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.registerTypeAdapter(ParsedJob.class, new ParsedJob.JobDeserializer());
        gbuilder.registerTypeAdapter(ParsedJob.class, new ParsedJob.JobSerializer());
        Gson gson = gbuilder.create();
        List<ParsedJob> jbs = new LinkedList<>();
        for (ParsedJob pj : pjs) {
            JsonObject j = gson.toJsonTree(pj).getAsJsonObject();
            String val = j.get(key).getAsString();
            if (val.equals(value)) {
                jbs.add(gson.fromJson(j, ParsedJob.class));
            }
        }
        return jbs.toArray(new ParsedJob[0]);
    }
}
