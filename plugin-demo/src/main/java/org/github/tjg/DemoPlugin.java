package org.github.tjg;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class DemoPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        Task task = project.getTasks().create("docUpload", DocTask.class);
        task.dependsOn("clean");
        task.dependsOn("assemble");
    }
}