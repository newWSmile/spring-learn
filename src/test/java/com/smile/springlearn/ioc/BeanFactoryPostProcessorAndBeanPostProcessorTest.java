package com.smile.springlearn.ioc;

import com.smile.springlearn.bean.Car;
import com.smile.springlearn.bean.Person;
import com.smile.springlearn.beans.factory.support.DefaultListableBeanFactory;
import com.smile.springlearn.beans.factory.xml.XmlBeanDefinitionReader;
import com.smile.springlearn.common.CustomBeanFactoryPostProcessor;
import com.smile.springlearn.common.CustomBeanPostProcessor;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BeanFactoryPostProcessorAndBeanPostProcessorTest {


    @Test
    public void testBeanFactoryPostProcessor() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //在所有BeanDefinition 加载完成后 但是在bean实例化之前 修改BeanDefinition的属性值
        CustomBeanFactoryPostProcessor beanFactoryPostProcessor = new CustomBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        //name 在xml中是smile  在CustomBeanFactoryPostProcessor中被我们修改成了jane
        assertThat(person.getName()).isEqualTo("jane");
    }

    @Test
    public void testBeanPostProcessor() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //添加bean实例化后的处理器
        CustomBeanPostProcessor beanPostProcessor = new CustomBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car);
        //在CustomBeanPostProcessor.postProcessBeforeInitialization中替换成了兰博基尼
        assertThat(car.getBrand()).isEqualTo("兰博基尼");
    }

}
