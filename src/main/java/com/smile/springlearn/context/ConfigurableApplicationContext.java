package com.smile.springlearn.context;

import com.smile.springlearn.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{

    /**
     * 刷新容器
     * @throws BeansException
     */
    void refresh() throws BeansException;

    /**
     * 关闭应用上下文
     */
    void close();


    /**
     * 注册一个钩子 关闭之前执行关闭容器等操作
     */
    void registerShutdownHook();

}
