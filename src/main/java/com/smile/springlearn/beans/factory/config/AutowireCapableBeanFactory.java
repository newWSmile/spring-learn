package com.smile.springlearn.beans.factory.config;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行BeanPostProcessor的前置方法即postProcessorsBeforeInitialization
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean,String beanName) throws BeansException;


    /**
     * 执行BeanPostProcessor的后置方法即postProcessorsAfterInitialization
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean,String beanName) throws BeansException;

}
