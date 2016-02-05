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
import java.net.MalformedURLException;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ua.pp.msk.cliqr.JobInfoImpl;
import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import ua.pp.msk.gradle.ext.CliQrExtension;
import ua.pp.msk.gradle.ext.JobInfoExtension;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class JobInfo extends DefaultTask {

    @TaskAction
    public void info(){
        getLogger().info("Starting job info task");
       PrintStream output = null;
        try {
        JobInfoExtension jix = getProject().getExtensions().findByType(JobInfoExtension.class);
        if (jix == null) {
            jix = new JobInfoExtension();
        }
        CliQrExtension cx = getProject().getExtensions().findByType(CliQrExtension.class);
        if (cx == null) {
            cx = new CliQrExtension();
        }

        getLogger().debug("CliQr host " + cx.getHost());
        getLogger().debug("CliQr user " + cx.getUser());
        getLogger().debug("CliQr apiKey " + cx.getApiKey());
        getLogger().debug("CliQr Job Id " + jix.getJobId());
        if (jix.getOutput() == null || jix.getOutput().length() == 0){
            output = System.out;
              getLogger().debug("CliQr Info output to stdout");
        } else {
            File of = new File(jix.getOutput());
            of.getParentFile().mkdirs();
            output = new PrintStream(new FileOutputStream(of));
              getLogger().debug("CliQr Info output to file " + jix.getOutput());
        }
      
        ua.pp.msk.cliqr.JobInfo  ji = new JobInfoImpl(cx.getHost(), cx.getUser(), cx.getApiKey());
        ji.setOutputStream(output);
        if (jix.getJobId() >= 0){
           ji.getJobInfo(jix.getJobId());
        } else {
            ji.getJobInfo();
        }
       } catch (FileNotFoundException ex) {
           getLogger().error("Cannot find file", ex);
       } catch (MalformedURLException | ClientSslException | ResponseException ex) {
            getLogger().error("Cannot get response", ex);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
}
