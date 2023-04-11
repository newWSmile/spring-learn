package com.smile.springlearn.ioc;

import com.smile.springlearn.beans.factory.BeanFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleBeanContainerTest {

    @Test
    public void testGetBean(){
//        BeanFactory beanFactory = new BeanFactory();
//        beanFactory.registerBean("helloService",new HelloService());
//        HelloService helloService = (HelloService)beanFactory.getBean("helloService");
//        assertNotNull(helloService);
//        assertEquals("Hello",helloService.sayHello());
    }

    @Test
    public void testFactoryBean(){

    }

}