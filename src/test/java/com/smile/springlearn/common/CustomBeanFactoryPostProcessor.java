package com.smile.springlearn.common;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.PropertyValue;
import com.smile.springlearn.beans.PropertyValues;
import com.smile.springlearn.beans.factory.ConfigurableListableBeanFactory;
import com.smile.springlearn.beans.factory.config.BeanDefinition;
import com.smile.springlearn.beans.factory.config.BeanFactoryPostProcessor;

public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("person");
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        //将person的 name 设置为jane
        propertyValues.addPropertyValue(new PropertyValue("name","jane"));
    }
}
