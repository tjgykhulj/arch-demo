package org.github.tjg;

import lombok.extern.slf4j.Slf4j;
import org.github.tjg.utils.GradleUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.util.Date;

import static org.apache.commons.lang3.SystemUtils.*;
import static org.github.tjg.utils.PrintUtils.initPrintUtils;
import static org.github.tjg.utils.PrintUtils.printSuccess;

@Slf4j
public class DocTask extends DefaultTask {

    @TaskAction
    public void doAction() {
        initPrintUtils();
        //printSuccess(MessageFormat.format(" - input: act={0}, branch={1}, commitId={2}, commitUser={3}", act, branch, commitId, commitUser));
        //printSuccess(" - plugin version: " + SwaggerRequest.SDK_VERSION);
        printSuccess(" - os version: " + OS_NAME + "." + OS_VERSION);
        printSuccess(" - java version: " + JAVA_VERSION);
        printSuccess(" - gradle version: " + getProject().getGradle().getGradleVersion());
        printSuccess(" - timestamp: " + new Date().toString());
        printSuccess("---------------------------------------");
        printSuccess("name: " + getName());
        printSuccess("path: " + getPath());
        printSuccess("pkg name: " + GradleUtils.getPackageNames(getProject()));
        printSuccess("all classes: " + GradleUtils.getAllClasses(getProject()));
    }
}
