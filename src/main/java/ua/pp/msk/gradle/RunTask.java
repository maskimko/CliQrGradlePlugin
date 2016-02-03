/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pp.msk.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 *
 * @author Maksym Shkolnyi aka maskimko
 */
public class RunTask extends DefaultTask {

    @TaskAction
    public void start() {
        getLogger().info("Starting Run task");
        RunTaskExtension rtx = getProject().getExtensions().findByType(RunTaskExtension.class);
        if (rtx == null) {
            rtx = new RunTaskExtension();
        }
        CliQrExtension cx = getProject().getExtensions().findByType(CliQrExtension.class);
        if (cx == null) {
            cx = new CliQrExtension();
        }

        getLogger().debug("CliQr host " + cx.getHost());
        getLogger().debug("CliQr user " + cx.getUser());
        getLogger().debug("CliQr apiKey " + cx.getApiKey());
        getLogger().debug("CliQr Job Id " + rtx.getJobId());
        getLogger().debug("CliQr Environment Pairs ");
        rtx.getEnvPairs().forEach((var, val) -> {
            getLogger().debug(String.format("%30s = %s", var, val));
        });

    }
}
