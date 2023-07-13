package com.smile.springlearn;

import com.smile.springlearn.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class InitAndDestroyMethodTest {

    @Test
    public void testInitAndDestroyMethod(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:init-and-destroy-method.xml");
        applicationContext.registerShutdownHook();  //或者手动关闭 applicationContext.close();
    }

}
