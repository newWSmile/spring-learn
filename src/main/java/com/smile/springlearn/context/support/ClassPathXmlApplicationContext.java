package com.smile.springlearn.context.support;

/**
 * xml文件的应用上下文
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{

    private String [] configLocations;

    /**
     * 从xml文件加载BeanDefinition 并且加载上下文
     * @param configLocations
     */
    public ClassPathXmlApplicationContext(String configLocations) {
        this(new String[]{configLocations});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }
}
