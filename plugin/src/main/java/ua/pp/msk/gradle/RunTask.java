package ua.pp.msk.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class RunTask extends DefaultTask {

        @TaskAction
            public void javaTask() {
                        System.out.println("Hello from MyJavaTask");
                            }

}
