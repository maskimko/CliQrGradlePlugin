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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobList {

    private ParsedJob[] jobs;
    int size;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ParsedJob[] getJobs() {
        return jobs;
    }

    public void setJobs(ParsedJob[] jobs) {
        this.jobs = jobs;
    }
    
    
    
    public static class JobListDeserializer implements JsonDeserializer<JobList> {

        @Override
        public JobList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jl = json.getAsJsonObject();
            JobList jobList = new JobList();
            ParsedJob[] pJobs = context.deserialize(jl.get("jobs"), ParsedJob[].class);
            int size = jl.get("size").getAsInt();
            jobList.setJobs(pJobs);
            jobList.setSize(size);
            return jobList;
        }
        
    }
    
    public static class JobListSerializer implements JsonSerializer<JobList>{

        @Override
        public JsonElement serialize(JobList src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jobList = new JsonObject();
            JsonElement jobs = context.serialize(src.getJobs());
            jobList.add("jobs", jobs);
            jobList.addProperty("size", src.getSize());
            return jobList;
        }
        
    }
            
}
