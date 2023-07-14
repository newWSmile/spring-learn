package com.smile.springlearn.context.support;

import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.factory.ConfigurableListableBeanFactory;
import com.smile.springlearn.beans.factory.config.BeanFactoryPostProcessor;
import com.smile.springlearn.beans.factory.config.BeanPostProcessor;
import com.smile.springlearn.context.ConfigurableApplicationContext;
import com.smile.springlearn.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * 抽象应用上下文
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    @Override
    public void refresh() throws BeansException {
        //创建BeanFactory 并加载BeanDefinition
        refreshBeanFactory();
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        //添加ApplicationContextAwareProcessor 让继承ApplicationContextAware的bean能感知ApplicationContext应用上下文
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        //在执行bean的实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);
        //BeanPostProcessor 需要提前与其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        //提前简单的实例化 单例bean
        beanFactory.preInstantiateSingletons();

    }

    /**
     * BeanPostProcessor 需要提前与其他bean实例化之前注册
     * @param beanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }

    }

    /**
     * 在bean实例化之前，执行BeanFactoryPostProcessor
     * @param beanFactory
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }


    @Override
    public Object getBean(String beanName) throws BeansException {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name,requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 创建BeanFactory 并加载BeanDefinition
     * @throws BeansException
     */
    protected abstract void refreshBeanFactory() throws BeansException;

    public void close(){
        doClose();
    }

    protected void doClose(){
        destroyBeans();
    }

    protected void destroyBeans(){
        getBeanFactory().destroySingletons();
    }


    public void registerShutdownHook() {
        Thread shutdownHook = new Thread(this::doClose);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
