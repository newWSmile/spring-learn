package com.smile.springlearn.service;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.BeanFactory;
import com.smile.springlearn.beans.factory.BeanFactoryAware;
import com.smile.springlearn.context.ApplicationContext;
import com.smile.springlearn.context.ApplicationContextAware;

public class HelloService implements ApplicationContextAware, BeanFactoryAware {

    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    public String sayHello(){
        System.out.println("hello...");
        return "Hello";
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
