package com.smile.springlearn.beans.factory;

import com.smile.springlearn.beans.BeansException;

import java.util.HashMap;
import java.util.Map;

public interface BeanFactory {

    /**
     * 获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) throws BeansException;


    /**
     * 根据名称和类型查找bean
     * @param name
     * @param requiredType
     * @return
     * @param <T>
     * @throws BeansException
     */
    <T> T getBean(String name,Class<T> requiredType) throws BeansException;

}
