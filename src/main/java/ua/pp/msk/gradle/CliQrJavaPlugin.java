package ua.pp.msk.gradle;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

public class CliQrJavaPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        //target.task(new HashMap<String, Object>(){{put("type", DummyTask.class);}}, "applytask");
        target.getTasks().create("infoTask", InfoTask.class);
        target.getTasks().create("runTask", RunTask.class);
        target.getExtensions().create("cliqr", CliQrPluginExtension.class);
      
    }

}
