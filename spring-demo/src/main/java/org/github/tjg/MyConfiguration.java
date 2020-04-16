package org.github.tjg;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({org.github.tjg.InitialConfig.class,
        org.github.tjg.InitialTask.class})
public @interface MyConfiguration {
}
