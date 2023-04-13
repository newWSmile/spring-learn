package com.smile.springlearn.beans.factory;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.config.AutowireCapableBeanFactory;
import com.smile.springlearn.beans.factory.config.BeanDefinition;
import com.smile.springlearn.beans.factory.config.BeanPostProcessor;
import com.smile.springlearn.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

    /**
     * 根据名称查找BeanDefinition
     * @param beanName
     * @return
     * @throws BeansException
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;


    /**
     * 提前实例化所有单例实例
     * @throws BeansException
     */
    void preInstantiateSingletons() throws BeansException;

    /**
     * 新增BeanPostProcessor
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}
