package com.smile.springlearn.ioc;

import com.smile.springlearn.bean.Car;
import com.smile.springlearn.bean.Person;
import com.smile.springlearn.beans.BeansException;
import com.smile.springlearn.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextTest {

    @Test
    public void testApplicationContext() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);
        //在CustomBeanFactoryPostProcessor改成了jane
        assertThat(person.getName()).isEqualTo("jane");

        Car car = applicationContext.getBean("car", Car.class);
        System.out.println(car);
        //在CustomBeanPostProcessor改成了兰博基尼
        assertThat(car.getBrand()).isEqualTo("兰博基尼");

    }

}
