package com.smile.springlearn.context;

import com.smile.springlearn.beans.factory.HierarchicalBeanFactory;
import com.smile.springlearn.beans.factory.ListableBeanFactory;
import com.smile.springlearn.core.io.ResourceLoader;

/**
 * 应用上下文
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {

}
