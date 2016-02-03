package ua.pp.msk.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class RunTask extends DefaultTask {

    @TaskAction
    public void dummyTask() {
        System.out.println("It is a very dummy plugin yet. Hello!!!");
        //getProject().getPlugins().apply(null);
    }
    
    @TaskAction
    public void cliqrRun() {
        System.out.println("Running CliQr Job");
    }

}
