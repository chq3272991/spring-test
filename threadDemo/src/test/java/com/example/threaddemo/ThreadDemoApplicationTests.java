package com.example.threaddemo;

import com.example.threaddemo.service.MyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutionException;

@SpringBootTest
class ThreadDemoApplicationTests {

    @Autowired
    private MyService myService;

    @Test
    void contextLoads() {
    }

    @Test
    void testTask() throws InterruptedException {
        // 非多线程，每次睡眠2秒，总耗时40秒
        for (int i = 0 ; i < 20 ; i++) {
            myService.executeTask();
        }
        // myService.executeAsyncTask支持多线程，因此循环20次，总时间不会是累加，是并发执行
        for (int i = 0 ; i < 20 ; i++) {
            myService.executeAsyncTask();
        }
    }

    @Test
    void testCompletableFutureTask() throws InterruptedException, ExecutionException {
        // 测试异步编程, 使用自带默认线程池
        // myService.performTask();
        // 测试异步编程,使用自定义线程池
        myService.performTask2();
        // 测试并发编程
        // myService.performTask3();
    }
}
