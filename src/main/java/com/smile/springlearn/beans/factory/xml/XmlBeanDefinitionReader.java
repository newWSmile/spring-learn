package com.smile.springlearn.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.beans.PropertyValue;
import com.smile.springlearn.beans.factory.config.BeanDefinition;
import com.smile.springlearn.beans.factory.config.BeanReference;
import com.smile.springlearn.beans.factory.support.AbstractBeanDefinitionReader;
import com.smile.springlearn.beans.factory.support.BeanDefinitionRegistry;
import com.smile.springlearn.core.io.Resource;
import com.smile.springlearn.core.io.ResourceLoader;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * 读取配置在xml文件中的bean定义信息
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }


    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }


    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            InputStream inputStream = resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) {
        Document document = XmlUtil.readXML(inputStream);
        Element root = document.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i) instanceof Element) {
                if (BEAN_ELEMENT.equals(((Element) childNodes.item(i)).getNodeName())) {
                    //解析bean标签
                    Element bean = (Element) childNodes.item(i);
                    String id = bean.getAttribute(ID_ATTRIBUTE);
                    String name = bean.getAttribute(NAME_ATTRIBUTE);
                    String className = bean.getAttribute(CLASS_ATTRIBUTE);
                    String initMethodName = bean.getAttribute(INIT_METHOD_ATTRIBUTE);
                    String destroyMethodName = bean.getAttribute(DESTROY_METHOD_ATTRIBUTE);

                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new BeansException("Cannot find class [" + className + "]");
                    }
                    //id 优先于name
                    String beanName = StrUtil.isNotEmpty(id) ? id : name;
                    if (StrUtil.isEmpty(beanName)) {
                        beanName = StrUtil.lowerFirst(clazz.getSimpleName());
                    }
                    BeanDefinition beanDefinition = new BeanDefinition(clazz);
                    beanDefinition.setInitMethodName(initMethodName);
                    beanDefinition.setDestroyMethodName(destroyMethodName);

                    for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                        if (bean.getChildNodes().item(j) instanceof Element) {
                            if (PROPERTY_ELEMENT.equals(((Element) bean.getChildNodes().item(j)).getNodeName())) {
                                //解析property 标签
                                Element property = (Element) bean.getChildNodes().item(j);
                                String nameAttribute = property.getAttribute(NAME_ATTRIBUTE);
                                String valueAttribute = property.getAttribute(VALUE_ATTRIBUTE);
                                String refAttribute = property.getAttribute(REF_ATTRIBUTE);

                                if (StrUtil.isEmpty(nameAttribute)) {
                                    throw new BeansException("The name attribute cannot be null or empty");
                                }
                                Object value = valueAttribute;
                                if (StrUtil.isNotEmpty(refAttribute)) {
                                    value = new BeanReference(refAttribute);
                                }
                                PropertyValue propertyValue = new PropertyValue(nameAttribute, value);
                                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                            }
                        }
                    }
                    //beanName不能重复
                    if (getRegistry().containsBeanDefinition(beanName)){
                        throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
                    }
                    //注册BeanDefinition
                    getRegistry().registerBeanDefinition(beanName,beanDefinition);
                }
            }

        }
    }
}
