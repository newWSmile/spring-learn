package com.smile.springlearn.ioc;

import com.smile.springlearn.bean.Person;
import com.smile.springlearn.beans.PropertyValue;
import com.smile.springlearn.beans.PropertyValues;
import com.smile.springlearn.beans.factory.config.BeanDefinition;
import com.smile.springlearn.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void testPopulateBeanWithPropertyValues() throws Exception{
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name","smile"));
        propertyValues.addPropertyValue(new PropertyValue("age",27));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class,propertyValues);
        beanFactory.registerBeanDefinition("person",beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("smile");
        assertThat(person.getAge()).isEqualTo(27);
    }

}
