package com.smile.springlearn.beans.factory.support;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.config.BeanDefinition;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * cglib实现Bean的实例化策略
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    /**
     * 使用CGLIB动态生成子类
     *
     * @param beanDefinition
     * @return
     * @throws BeansException
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> methodProxy.invokeSuper(o, objects));
        return enhancer.create();
    }
}
