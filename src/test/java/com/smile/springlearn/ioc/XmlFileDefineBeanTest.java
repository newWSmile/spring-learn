package com.smile.springlearn.ioc;

import com.smile.springlearn.bean.Car;
import com.smile.springlearn.bean.Person;
import com.smile.springlearn.beans.factory.support.DefaultListableBeanFactory;
import com.smile.springlearn.beans.factory.xml.XmlBeanDefinitionReader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlFileDefineBeanTest {

    @Test
    public void testXmlFile() throws Exception{
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("smile");
        assertThat(person.getCar().getBrand()).isEqualTo("奔驰");

        Car car = beanFactory.getBean("car", Car.class);
        System.out.println(car);
        assertThat(car.getBrand()).isEqualTo("奔驰");

    }

}
