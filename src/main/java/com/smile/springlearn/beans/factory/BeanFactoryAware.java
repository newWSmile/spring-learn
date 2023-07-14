package com.smile.springlearn.beans.factory;

import com.smile.springlearn.beans.BeansException;
import org.springframework.beans.factory.Aware;

/***
 * 实现此接口 就能感知所属的BeanFactor
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
