package org.github.tjg;

import lombok.Data;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

@Data
public class DocTask extends DefaultTask {

    private int taskType;

    @TaskAction
    public void doAction() {
        System.out.println(taskType);
    }
}
