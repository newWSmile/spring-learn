package com.smile.springlearn.context;

import com.smile.springlearn.beans.BeansException;
import org.springframework.beans.factory.Aware;

/**
 * 实现此接口 就能获取到ApplicationContext上下文
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
