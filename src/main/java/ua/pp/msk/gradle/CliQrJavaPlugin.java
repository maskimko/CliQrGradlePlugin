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
