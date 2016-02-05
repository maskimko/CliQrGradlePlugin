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
package ua.pp.msk.gradle;

import ua.pp.msk.gradle.task.ApplicationInfo;
import ua.pp.msk.gradle.task.AppRun;
import ua.pp.msk.gradle.ext.CliQrExtension;
import ua.pp.msk.gradle.ext.AppRunExtension;
import org.gradle.api.Project;
import org.gradle.api.Plugin;
import ua.pp.msk.gradle.ext.AppInfoExtension;
import ua.pp.msk.gradle.ext.JobInfoExtension;
import ua.pp.msk.gradle.task.JobInfo;

public class CliQrJavaPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        //target.task(new HashMap<String, Object>(){{put("type", DummyTask.class);}}, "applytask");
        target.getTasks().create("appInfo", ApplicationInfo.class);
        target.getTasks().create("jobInfo", JobInfo.class);
        target.getTasks().create("appRun", AppRun.class);
        target.getExtensions().create("runConf", AppRunExtension.class);
        target.getExtensions().create("infoConf", AppInfoExtension.class);
        target.getExtensions().create("jobConf", JobInfoExtension.class);
        target.getExtensions().create("cliqr", CliQrExtension.class);

    }

}
