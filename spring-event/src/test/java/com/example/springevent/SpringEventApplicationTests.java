package com.example.springevent;

import com.example.springevent.controller.IndexController;
import com.example.springevent.event.MyEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class SpringEventApplicationTests {

    @Test
    void contextLoads() {
    }
    
    // 调用controller中的方法，发布一个自定义事件
    @Test
    void testPublishEvent() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringEventApplication.class);
        String name = "张三";
        context.getBean(IndexController.class).index(name);
    }
}
