/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.cliqr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
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
