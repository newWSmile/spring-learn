package com.smile.springlearn.beans.factory.support;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.DisposableBean;
import com.smile.springlearn.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 传说中的一级缓存
     */
    private Map<String, Object> singletonObjects = new HashMap<>();

    /**
     * 销毁方法集合
     */
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }


    protected void registerDisposableBean(String beanName,DisposableBean bean){
        disposableBeans.put(beanName,bean);
    }

    public void destroySingletons(){
        ArrayList<String> beanNames = new ArrayList<>(disposableBeans.keySet());
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception",e);
            }
        }
    }
}
