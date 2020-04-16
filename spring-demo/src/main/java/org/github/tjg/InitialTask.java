package org.github.tjg;

import lombok.extern.slf4j.Slf4j;
import org.github.tjg.task.Task;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InitialTask implements BeanPostProcessor {

    private Set<Class<?>> classes = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>(64));

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (!classes.contains(targetClass)) {
            classes.add(targetClass);
            ReflectionUtils.doWithMethods(targetClass, method -> {
                Task task = AnnotationUtils.getAnnotation(method, Task.class);
                if (task != null) {
                    processTask(task, method, bean);
                }
            });
        }
        return bean;
    }

    private void processTask(Task scheduled, Method method, Object bean) {
        log.info("task name = {} begin", scheduled.value());
        Class<?>[] parameterTypes = method.getParameterTypes();
        int parameterLength = parameterTypes.length;
        Assert.isTrue(void.class == method.getReturnType(),
                "Only void returning methods may be annotated with @Task");
        Assert.isTrue(parameterLength < 2,
                "Only no-arg or one-arg methods may be annotated with @Gavin");

        // execute task
        try {
            method.invoke(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            log.info("task name = {} finish", scheduled.value());
        }

    }
}
