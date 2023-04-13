package com.smile.springlearn.beans.factory.config;

import com.smile.springlearn.beans.factory.HierarchicalBeanFactory;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory,SingletonBeanRegistry {

    /**
     * 新增BeanPostProcessor
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);


}
