/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class ParsedJob {
    
    private final int id;
    private URL resource;
    private String name;
    private JobStatus status;
    private int appId;
    private String appVersion;
    private String appName;
    private String cloud;
    private int depInitiatingUserId;
    private int environmentId;
    private String environment;
    private Calendar startTime;
    private Calendar endTime;
    
    public ParsedJob(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public URL getResource() {
        return resource;
    }
    
    public void setResource(URL resource) {
        this.resource = resource;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public JobStatus getStatus() {
        return status;
    }
    
    public void setStatus(JobStatus status) {
        this.status = status;
    }
    
    public int getAppId() {
        return appId;
    }
    
    public void setAppId(int appId) {
        this.appId = appId;
    }
    
    public String getAppVersion() {
        return appVersion;
    }
    
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    
    public String getAppName() {
        return appName;
    }
    
    public void setAppName(String appName) {
        this.appName = appName;
    }
    
    public String getCloud() {
        return cloud;
    }
    
    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
    
    public int getDepInitiatingUserId() {
        return depInitiatingUserId;
    }
    
    public void setDepInitiatingUserId(int depInitiatingUserId) {
        this.depInitiatingUserId = depInitiatingUserId;
    }
    
    public int getEnvironmentId() {
        return environmentId;
    }
    
    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public Calendar getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }
    
    public Calendar getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.resource);
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.status);
        hash = 29 * hash + Objects.hashCode(this.startTime);
        hash = 29 * hash + Objects.hashCode(this.endTime);
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParsedJob other = (ParsedJob) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    public static class JobDeserializer implements JsonDeserializer<ParsedJob> {
        
        @Override
        public ParsedJob deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ParsedJob pj;
            JsonObject jobJson = json.getAsJsonObject();
            
            int jobId = jobJson.get("id").getAsInt();
            pj = new ParsedJob(jobId);
            URL resource = null;
            try {
                resource = new URL(jobJson.get("resource").getAsString());
            } catch (MalformedURLException ex) {
                LoggerFactory.getLogger(this.getClass()).warn("Cannot parse URL " + jobJson.get("resource").getAsString());
            }
            pj.setResource(resource);
            //We skipped permissions json section. It can be added in the future
            pj.setName(jobJson.get("name").getAsString());
            pj.setStatus(JobStatus.valueOf(jobJson.get("status").getAsString()));
            pj.setAppId(jobJson.get("appId").getAsInt());
            pj.setAppVersion(jobJson.get("appVersion").getAsString());
            pj.setAppName(jobJson.get("appName").getAsString());
            pj.setCloud(jobJson.get("cloud").getAsString());
            pj.setDepInitiatingUserId(jobJson.get("depInitiatingUserId").getAsInt());
            pj.setEnvironmentId(jobJson.get("environmentId").getAsInt());
            pj.setEnvironment(jobJson.get("environment").getAsString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Calendar startTime = Calendar.getInstance();
            String st = jobJson.get("startTime").getAsString();
            if (st.equals("N/A")) {
                pj.setStartTime(null);
            } else {
                startTime.setTimeZone(TimeZone.getTimeZone("UTC"));
                
                try {
                    startTime.setTime(sdf.parse(st));
                    pj.setStartTime(startTime);
                } catch (ParseException pe) {
                    LoggerFactory.getLogger(this.getClass()).warn(String.format("Cannot cannot parse time %s %s", st, pe.getMessage()));
                }
            }
            Calendar endTime = Calendar.getInstance();
            String et = jobJson.get("endTime").getAsString();
            if (et.equals("N/A")) {
                pj.setEndTime(null);
            } else {
                endTime.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    endTime.setTime(sdf.parse(et));
                    pj.setEndTime(endTime);
                } catch (ParseException pe) {
                    LoggerFactory.getLogger(this.getClass()).warn(String.format("Cannot cannot parse time %s %s", et, pe.getMessage()));
                }
            }
            return pj;
            
        }
        
    }

    public static class JobSerializer implements JsonSerializer<ParsedJob> {
        
        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        
        @Override
        public JsonElement serialize(ParsedJob src, Type typeOfSrc, JsonSerializationContext context) {
            
            JsonObject job = new JsonObject();
            job.addProperty("id", src.getId());
            job.addProperty("resource", src.getResource().toString());
            job.addProperty("name", src.getName());
            //TODO implements permissions scan
            job.addProperty("status", src.getStatus().toString());
            job.addProperty("appId", src.getAppId());
            job.addProperty("appVersion", src.getAppVersion());
            job.addProperty("appName", src.getAppName());
            job.addProperty("cloud", src.getCloud());
            job.addProperty("depInitiatingUserId", src.getDepInitiatingUserId());
            job.addProperty("environmentId", src.getEnvironmentId());
            job.addProperty("environment", src.getEnvironment());
            if (src.getStartTime() != null) {
                job.addProperty("startTime", sdf.format(src.getStartTime().getTime()));
            } else {
                 job.addProperty("startTime", "N/A");
            }
             if (src.getEndTime() != null) {
                 job.addProperty("endTime", sdf.format(src.getEndTime().getTime()));
             } else {
                 job.addProperty("endTime", "N/A");
             }
            return job;
        }
        
    }
}
