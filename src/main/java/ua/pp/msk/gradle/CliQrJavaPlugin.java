package ua.pp.msk.gradle;

import org.gradle.api.Project;
import org.gradle.api.Plugin;
import org.gradle.api.Task;

public class CliQrJavaPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        Task task = target.task("dummyTask");
    }

}
