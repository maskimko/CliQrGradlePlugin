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
package ua.pp.msk.gradle.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import ua.pp.msk.gradle.ext.CliQrExtension;
import ua.pp.msk.gradle.ext.AppRunExtension;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ua.pp.msk.cliqr.JobInfoImpl;
import ua.pp.msk.cliqr.PostProcessorImpl;
import ua.pp.msk.cliqr.RunJob;
import ua.pp.msk.cliqr.RunJobImpl;
import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.MissingParameterException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import ua.pp.msk.cliqr.exceptions.RunJobException;
import ua.pp.msk.cliqr.job.JobStatus;
import ua.pp.msk.cliqr.job.ParsedJob;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class AppRun extends DefaultTask {
    
    @TaskAction
    public void start() {
        getLogger().info("Starting Run task");
        AppRunExtension rtx = getProject().getExtensions().findByType(AppRunExtension.class);
        if (rtx == null) {
            rtx = new AppRunExtension();
        }
        CliQrExtension cx = getProject().getExtensions().findByType(CliQrExtension.class);
        if (cx == null) {
            cx = new CliQrExtension();
        }
        
        getLogger().debug("CliQr host " + cx.getHost());
        getLogger().debug("CliQr user " + cx.getUser());
        getLogger().debug("CliQr apiKey " + cx.getApiKey());
        getLogger().debug("CliQr App Id " + rtx.getAppId());
        getLogger().debug("CliQr Service Tier Id " + rtx.getServiceTierId());
        getLogger().debug("CliQr Environment Pairs ");
        rtx.getParams().forEach((var, val) -> {
            getLogger().debug(String.format("%30s = %s", var, val));
        });
        RunJob rj = null;
        try {
            RunJobImpl rji = new RunJobImpl(cx.getHost(), cx.getUser(), cx.getApiKey());
            URL cqUrl = new URL("https://" + cx.getHost());
            rji.setProcessor(new PostProcessorImpl(cqUrl, cx.getUser(), cx.getApiKey()));
            rj = rji;
            
        } catch (ClientSslException ex) {
            getLogger().error("SSL Error", ex);
        } catch (MalformedURLException ex) {
            getLogger().error("Malformed URL", ex);
        }
        try {
            if (rj != null) {
                String response = rj.startJob(rtx.getAppId(), rtx.getServiceTierId(), rtx.getAppName(), rtx.getVersion(), rtx.getCloud(),
                        rtx.getNetwork(), rtx.getEnvironment(), rtx.getInstanceSize(), rtx.isPublicIp(), rtx.getParams());
                getLogger().debug("Server response:\n" + response);
                cx.setLocation(response);
                if (rtx.getWait() >=0 && response != null && response.length()> 0) {
                    try {
                    String[] splitedUrl = response.split("/");
                    int jid = Integer.parseInt(splitedUrl[splitedUrl.length-1]);
                        boolean result = wait(jid, rtx, cx);
                        System.out.println((result) ?"Job successfully started" : "Job has failed to start");
                    } catch (NumberFormatException ex){
                        getLogger().error("Cannot parse job id from response " + response);
                    }
                }
            } else {
                throw new RunJobException("Run job has been not initialized");
            }
        } catch (RunJobException | MissingParameterException ex) {
            getLogger().error("Cannot run CliQr app " + rtx.getAppId(), ex);
        }
    }
    
    private boolean wait(int jobId, AppRunExtension rtx, CliQrExtension cx) {
        PrintStream output = null;
        boolean successful = false;        
        
        int timeout = rtx.getWait();
        try {
            if (rtx.getOutput() == null || rtx.getOutput().length() == 0) {
                output = System.out;
                getLogger().debug("CliQr Info output to stdout");
            } else {
                File of = new File(rtx.getOutput());
                of.getParentFile().mkdirs();
                output = new PrintStream(new FileOutputStream(of));
                getLogger().debug("CliQr Info output to file " + rtx.getOutput());
            }
            ua.pp.msk.cliqr.JobInfo ji = new JobInfoImpl(cx.getHost(), cx.getUser(), cx.getApiKey());
            ParsedJob parsedJobInfo = null;            
            while (timeout >= 0) {
                parsedJobInfo = ji.getParsedJobInfo(jobId);
                if (parsedJobInfo.getStatus().equals(JobStatus.JobInProgress)) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        getLogger().warn("Waiting for a job to finish has been interrupted");
                    }
                    timeout -= 10;
                    continue;
                } else if (parsedJobInfo.getStatus().equals(JobStatus.JobRunning)) {
                    successful = true;
                    output.println(parsedJobInfo);
                    break;
                } else {
                    String logMessage = String.format("Job %d failed. Job status %s\n\tTimeStamp: %tF\n\tError message %s",
                            jobId, parsedJobInfo.getStatus(), Calendar.getInstance().getTime(),
                            parsedJobInfo.getMessage());
                    getLogger().error(logMessage);
                    output.println(logMessage);
                }
            }
        } catch (MalformedURLException ex) {
            getLogger().error("Bad URL" + ex.getMessage(), ex);
        } catch (ClientSslException | ResponseException ex) {
            getLogger().error(ex.getMessage(), ex);
        } catch (FileNotFoundException ex) {
            getLogger().error("Cannot find file", ex);
        } finally {
            if (output != null ) output.close();
        }
        return successful;
    }
}
