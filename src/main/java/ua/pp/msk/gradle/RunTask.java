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
        CliQrPluginExtension extension = getProject().getExtensions().findByType(CliQrPluginExtension.class);
        if (extension == null) {
            extension = new CliQrPluginExtension();
        }

        getLogger().debug("CliQr host " + extension.getHost());
        getLogger().debug("CliQr user " + extension.getUser());
        getLogger().debug("CliQr apiKey " + extension.getApiKey());
        getLogger().debug("CliQr Job Id " + extension.getJobId());
        getLogger().debug("CliQr Environment Pairs ");
        extension.getEnvPairs().forEach((var, val) -> {
            getLogger().debug(String.format("%30s = %s", var, val));
        });

    }
}
