package com.smile.springlearn.beans.factory.config;

/**
 * 单例注册表
 */
public interface SingletonBeanRegistry {

    /**
     * 获取单例
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

}
