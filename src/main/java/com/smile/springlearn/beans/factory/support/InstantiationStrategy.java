package com.smile.springlearn.beans.factory.support;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.config.BeanDefinition;

/**
 * Bean的实例化策略
 */
public interface InstantiationStrategy {


    Object instantiate(BeanDefinition beanDefinition) throws BeansException;

}
