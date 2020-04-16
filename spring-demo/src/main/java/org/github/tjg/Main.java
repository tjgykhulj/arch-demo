package org.github.tjg;

import org.github.tjg.model.DemoBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@MyConfiguration
public class Main {

    public static void main(String args[]) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);
        DemoBean bean = ctx.getBean(DemoBean.class);
        System.out.println(bean.getA());
    }
}
