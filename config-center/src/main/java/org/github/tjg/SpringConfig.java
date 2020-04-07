package org.github.tjg;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
//@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Bean
    public InitialConfig initialConfig() {
        return new InitialConfig();
    }

    public static void main(String args[]) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        MyBean myBean = ctx.getBean(MyBean.class);
        System.out.println(myBean.getA());
    }
}
