/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.pp.msk.gradle.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ua.pp.msk.cliqr.AppInfo;
import ua.pp.msk.cliqr.AppInfoImpl;
import ua.pp.msk.cliqr.JobInfo;
import ua.pp.msk.cliqr.RunJob;
import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.ResponseException;
import ua.pp.msk.gradle.ext.AppInfoExtension;
import ua.pp.msk.gradle.ext.AppRunExtension;
import ua.pp.msk.gradle.ext.CliQrExtension;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class ApplicationInfo extends DefaultTask {
 
    
    @TaskAction
    public void info(){
        getLogger().info("Starting info task");
       PrintStream output = null;
        try {
        AppInfoExtension aix = getProject().getExtensions().findByType(AppInfoExtension.class);
        if (aix == null) {
            aix = new AppInfoExtension();
        }
        CliQrExtension cx = getProject().getExtensions().findByType(CliQrExtension.class);
        if (cx == null) {
            cx = new CliQrExtension();
        }

        getLogger().debug("CliQr host " + cx.getHost());
        getLogger().debug("CliQr user " + cx.getUser());
        getLogger().debug("CliQr apiKey " + cx.getApiKey());
        getLogger().debug("CliQr App Id " + aix.getAppId());
        if (aix.getOutput() == null || aix.getOutput().length() == 0){
            output = System.out;
              getLogger().debug("CliQr Info output to stdout");
        } else {
            File of = new File(aix.getOutput());
            of.getParentFile().mkdirs();
            output = new PrintStream(new FileOutputStream(of));
              getLogger().debug("CliQr Info output to file " + aix.getOutput());
        }
      
       String response = null;
        AppInfo  ai = new AppInfoImpl(cx.getHost(), cx.getUser(), cx.getApiKey());
        if (aix.getAppId() >= 0){
           response = ai.getAppInfo(aix.getAppId());
        } else {
            response = ai.getAppInfo();
        }
          output.print(response);
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
