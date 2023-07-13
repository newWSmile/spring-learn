package com.smile.springlearn.bean;

import com.smile.springlearn.beans.factory.DisposableBean;
import com.smile.springlearn.beans.factory.InitializingBean;
import lombok.Data;

@Data
public class Person implements InitializingBean, DisposableBean {

    private String name;

    private Integer age;


    private Car car;

    public void smileCustomInitMethod(){
        System.out.println("执行配置文件中smile自定义的smileCustomInitMethod()方法,初始化...");
    }

    public void smileCustomDestroyMethod(){
        System.out.println("执行配置文件中smile自定义的smileCustomDestroyMethod()方法,开始销毁...");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean#afterPropertiesSet()方法中初始化出来...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("InitializingBean#destroy()方法中销毁...");
    }
}
