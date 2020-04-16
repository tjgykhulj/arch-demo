package org.github.tjg.utils;

import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.gradle.api.Project;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
class ClassFinder {

    private Project project;
    private ClassLoader classLoader;
    private Map<Class<? extends Annotation>, Set<Class<?>>> classCache = new HashMap<>();

    public ClassFinder(Project project) {
        this.project = project;
        initClassLoader(null);
    }

    public ClassFinder(Project project, String module) {
        this.project = project;
        initClassLoader(module);
    }

    private void initClassLoader(String module) {
        List<File> classpath = new ArrayList<>();

        List<Project> projects = new ArrayList<>();
        Project resolveProject = GradleUtils.getModuleProject(project, module);
        projects.add(resolveProject);
        if (resolveProject == resolveProject.getRootProject()){
            Map<String, Project> childProjects = resolveProject.getChildProjects();
            projects.addAll(childProjects.values());
        }

        for (Project oneProject : projects){
            try {
                classpath.addAll(oneProject.getConfigurations().getByName("compile").resolve());
                String buildDir = oneProject.getBuildDir().getPath();
                File dirFile1 = new File(buildDir + "/classes/java/main");
                if (dirFile1.exists()){
                    classpath.add(dirFile1);
                }
                File dirFile2 = new File(buildDir + "/classes/main");
                if (dirFile2.exists()){
                    classpath.add(dirFile2);
                }
            }catch (Exception e){
                //ignore any exception
            }
        }

        List<URL> urls = new ArrayList<>();
        for (File file : classpath) {
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                log.info("DefaultClassFinder", e);
            }
        }
        this.classLoader =
                new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
    }

    public <T extends Annotation> Set<Class<?>> getValidClasses(Class<T> clazz, List<String> packages) {
        if (classCache.containsKey(clazz)) {
            return classCache.get(clazz);
        }

        Set<Class<?>> classes = new HashSet<>();
        if (packages != null) {
            for (String location : packages) {
                Set<Class<?>> c = new Reflections(this.classLoader, location).getTypesAnnotatedWith(clazz);
                classes.addAll(c);
            }
        } else {
            log.error("locations is null");
        }

        classCache.put(clazz, classes);
        return classes;
    }

    public Set<Class<?>> getAllClasses(List<String> packages) {
        Set<Class<?>> set = new HashSet<>();
        if (packages == null) {
            return set;
        }
        for (String pkg: packages) {
            try {
                ClassPath.from(this.classLoader)
                        .getTopLevelClassesRecursive(pkg)
                        .stream()
                        .map(ClassPath.ClassInfo::load)
                        .forEach(set::add);
            } catch (IOException e) {
                log.error("load service interface from package: {}", pkg, e);
            }
        }
        return set;
    }

    public ClassLoader classLoader() {
        return this.classLoader;
    }
}
