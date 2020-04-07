package org.github.tjg;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.util.Set;

public class MyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("dove", DocTaskExtention.class);
        {
            Task task = project.getTasks().create("docUpload", DocTask.class);
            task.setProperty("taskType", 1);
            configDependsOn(project, task);
        }
        {
            Task task = project.getTasks().create("docUpload2", DocTask.class);
            task.setProperty("taskType", 2);
            configDependsOn(project, task);
        }
    }

    /**
     * 设置任务的依赖关系
     * @param project
     * @param task
     */
    private void configDependsOn(Project project, Task task) {
        task.dependsOn("clean");
        task.dependsOn("assemble");
        task.mustRunAfter("assemble");
        Set<Task> tasks = project.getTasksByName("assemble", true);
        for (Task t : tasks){
            t.dependsOn(task.getPath());
            t.mustRunAfter(task.getPath());
            t.mustRunAfter("clean");
        }
    }
}