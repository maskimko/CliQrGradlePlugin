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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import ua.pp.msk.cliqr.job.JobFilter;
import ua.pp.msk.cliqr.job.JobList;
import ua.pp.msk.cliqr.job.ParsedJob;
import java.io.PrintStream;
import java.net.MalformedURLException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobInfoImpl extends InfoImpl implements JobInfo {

  
    private PrintStream ops;
    
    public JobInfoImpl(String host, String user, String apiKey, PrintStream ps){
        super(host, user, apiKey);
        ops = ps;
    }
    

    public JobInfoImpl(String host, String user, String apiKey) {
      super(host, user, apiKey);
      ops = System.out;
    }

    @Override
    public void getJobInfo(int jobNumber) throws MalformedURLException, ClientSslException, ResponseException {
       
        ops.println(getInfo("jobs/"+jobNumber));
    }

    @Override
    public ParsedJob getParsedJobInfo(int jobNumber) throws MalformedURLException, ClientSslException, ResponseException {
       ParsedJob pj = null;
        String info = getInfo("jobs/"+jobNumber);
          GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.registerTypeAdapter(ParsedJob.class, new ParsedJob.JobDeserializer());
         Gson gson = gbuilder.create();
            try { 
            pj = gson.fromJson(info, ParsedJob.class);
        } catch (JsonSyntaxException je){
            LoggerFactory.getLogger(this.getClass()).error("Cannot deserialize jobs", je);
        }
            return pj;
    }
    
    

    @Override
    public void getJobInfo() throws MalformedURLException, ClientSslException, ResponseException {
        
       ops.println(getInfo("jobs"));
    }

    @Override
    public PrintStream getOutputStream() {
        return ops;
    }

    @Override
    public void setOutputStream(PrintStream ops) {
        this.ops = ops;
    }

    
    JobList getJobList() throws MalformedURLException, ClientSslException, ResponseException{
        JobList jl = null;
        String jobsInfo = getInfo("jobs");
        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.registerTypeAdapter(ParsedJob.class, new ParsedJob.JobDeserializer());
        gbuilder.registerTypeAdapter(JobList.class, new JobList.JobListDeserializer());
        Gson gson = gbuilder.create();
        try { 
            jl = gson.fromJson(jobsInfo, JobList.class);
        } catch (JsonSyntaxException je){
            LoggerFactory.getLogger(this.getClass()).error("Cannot deserialize jobs", je);
        }
         return jl;
    }

    @Override
    public void getJobInfo(JobFilter... filters) throws MalformedURLException, ClientSslException, ResponseException {
        ParsedJob[] jobs = getJobList().getJobs();
        for (JobFilter jf : filters){
            jobs = jf.filter(jobs);
        }
        JobList jobList = new JobList();
        jobList.setJobs(jobs);
        jobList.setSize(jobs.length);
        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.setPrettyPrinting();
        gbuilder.registerTypeAdapter(ParsedJob.class, new ParsedJob.JobSerializer());
        gbuilder.registerTypeAdapter(JobList.class, new JobList.JobListSerializer());
        Gson gson = gbuilder.create();
        gson.toJson(jobList, ops);
    }
    
    
}
