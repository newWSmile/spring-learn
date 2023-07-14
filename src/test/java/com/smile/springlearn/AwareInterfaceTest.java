package com.smile.springlearn;

import com.smile.springlearn.context.support.ClassPathXmlApplicationContext;
import com.smile.springlearn.service.HelloService;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AwareInterfaceTest {

    @Test
    public void test() throws Exception{
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        HelloService helloService = applicationContext.getBean("helloService", HelloService.class);
        assertThat(helloService.getApplicationContext()).isNotNull();
        assertThat(helloService.getBeanFactory()).isNotNull();
    }

}
