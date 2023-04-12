package com.smile.springlearn.beans.factory.support;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class SimpleInstantiationStrategy implements InstantiationStrategy{
    /**
     * 简单的bean实例化策略 根据bean的无参构造函数实例化对象
     * @param beanDefinition
     * @return
     * @throws BeansException
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        Class beanClass = beanDefinition.getBeanClass();
        try {
            Constructor declaredConstructor = beanClass.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (Exception e) {
            throw new BeansException("Failed to instantiate ["+beanClass.getName()+"] ",e);
        }
    }
}
