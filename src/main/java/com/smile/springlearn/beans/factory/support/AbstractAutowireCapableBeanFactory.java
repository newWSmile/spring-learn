package com.smile.springlearn.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.PropertyValue;
import com.smile.springlearn.beans.factory.config.AutowireCapableBeanFactory;
import com.smile.springlearn.beans.factory.config.BeanDefinition;
import com.smile.springlearn.beans.factory.config.BeanPostProcessor;
import com.smile.springlearn.beans.factory.config.BeanReference;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        return doCreateBean(beanName, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            bean = createBeanInstance(beanDefinition);

            //为bean填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            //执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            bean = initializeBean(beanName,bean,beanDefinition);

        } catch (Exception e) {
            throw new BeansException("实例化bean失败", e);
        }
        addSingleton(beanName, bean);
        return bean;
    }

    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        //执行BeanPostProcessor的前置方法
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean,beanName);
        //Fixme:smile 后面会在此处执行bean的初始化方法
        invokeInitMethods(beanName,wrappedBean,beanDefinition);
        //执行BeanPostProcessor的后置方法
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean,beanName);
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
        //todo 后续实现
        System.out.println("执行bean【"+beanName+"】的初始化方法ing");
    }

    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValue()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference){
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }

                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values for bean: " + beanName, e);
        }

    }


    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result,beanName);
            if (null == current){
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result,beanName);
            if (null == current){
                return result;
            }
            result = current;
        }
        return result;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
