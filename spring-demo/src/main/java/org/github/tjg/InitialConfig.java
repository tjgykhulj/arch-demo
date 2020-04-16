package org.github.tjg;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.StringValueResolver;


public class InitialConfig implements BeanFactoryPostProcessor, PriorityOrdered, BeanNameAware, BeanFactoryAware {

    private String beanName;
    private BeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        StringValueResolver resolver = strVal -> {
            if (strVal.startsWith("${")) {
                System.out.println("resolve " + strVal);
            }
            return strVal.replace("value", "val");
        };
        // visitor有什么用？
        BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(resolver);
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        // 访问每一条bean，通过visitBeanDefinition会访问到bean的parentName\beanClassName\scope\propertyValues\constructArgs等信息
        for (String beanName : beanNames) {
            if (!StringUtils.equals(beanName, this.beanName)) {
                System.out.println("-->" + beanName);
                BeanDefinition def = beanFactory.getBeanDefinition(beanName);
                visitor.visitBeanDefinition(def);
            }
        }
        beanFactory.resolveAliases(resolver);
        beanFactory.addEmbeddedValueResolver(resolver);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }


    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
