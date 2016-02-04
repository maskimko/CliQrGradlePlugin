/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.gradle.task;

import ua.pp.msk.gradle.ext.CliQrExtension;
import ua.pp.msk.gradle.ext.AppRunExtension;
import java.net.MalformedURLException;
import java.net.URL;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import ua.pp.msk.cliqr.PostProcessorImpl;
import ua.pp.msk.cliqr.RunJob;
import ua.pp.msk.cliqr.RunJobImpl;
import ua.pp.msk.cliqr.exceptions.ClientSslException;
import ua.pp.msk.cliqr.exceptions.MissingParameterException;
import ua.pp.msk.cliqr.exceptions.RunJobException;

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
                rj.startJob(rtx.getAppId(), rtx.getServiceTierId(), rtx.getAppName(), rtx.getCloud(), rtx.getVpcId(),
                        rtx.getNetwork(), rtx.getEnvironment(), rtx.getInstanceSize(), rtx.isPublicIp(), rtx.getParams());
            } else {
                throw new RunJobException("Run job has been not initialized");
            }
        } catch (RunJobException | MissingParameterException ex) {
            getLogger().error("Cannot run CliQr app " + rtx.getAppId(), ex);
        }
    }
}
