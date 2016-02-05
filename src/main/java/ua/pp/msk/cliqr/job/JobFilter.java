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
