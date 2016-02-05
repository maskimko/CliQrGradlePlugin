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
