package com.smile.springlearn.beans.factory.config;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.ConfigurableListableBeanFactory;

/**
 * 允许自定义修改BeanDefinition的属性值
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有的BeanDefinition加载完成后，在实例化bean之前 提供修改BeanDefinition属性值的机制
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
