package ua.pp.msk.gradle;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

public class CliQrJavaPlugin implements Plugin<Project> {

        @Override
            public void apply(Project target) {
                        target.task("javaTask");
                            }

}
