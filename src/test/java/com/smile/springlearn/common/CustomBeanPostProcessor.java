package com.smile.springlearn.common;

import com.smile.springlearn.bean.Car;
import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.config.BeanPostProcessor;

import java.util.Objects;

public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("执行CustomBeanPostProcessor的postProcessBeforeInitialization 初始化之前的方法");
        if (Objects.equals("car",beanName)){
            //替换奔驰为兰博基尼
            System.out.println("CustomBeanPostProcessor.postProcessBeforeInitialization把奔驰替换成了兰博基尼");
            ((Car)bean).setBrand("兰博基尼");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("执行CustomBeanPostProcessor的postProcessAfterInitialization 初始化之后的方法");
        return bean;
    }
}
