package com.example.threaddemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/12/12
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // 核心线程数
        executor.setMaxPoolSize(10);  // 最大线程数
        executor.setQueueCapacity(25);  // 队列容量
        executor.setThreadNamePrefix("MyThread-");  // 线程名称前置
        executor.setTaskDecorator(new CustomTaskDecorator());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    class CustomTaskDecorator implements TaskDecorator {
        // 定义一个ThreadLocal, 这里也可以存储Map数据
        // 例如用户请求某任务，需要执行多线程，每个线程通过请求header的Open-key或者user信息，存储用户信息再在后续任务中使用
        private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

        @Override
        public Runnable decorate(Runnable runnable) {
            // 捕获当前的ThreadLocal的值
            String contextValue = "Task-" + Thread.currentThread().getId();
            threadLocal.set(contextValue);

            return () -> {
              try {
                  // 在执行任务之前，设置ThreadLocal
                  threadLocal.set(contextValue);
                  System.out.println(Thread.currentThread().getName() + " - Before execution, ThreadLocal value: " + threadLocal.get());

                  // 执行实际任务
                  runnable.run();
              }finally {
                  // 在任务执行完成后，清理ThreadLocal
                  threadLocal.remove();
                  System.out.println(Thread.currentThread().getName() + " - After execution, ThreadLocal cleared");
              }
            };
        }
    }
}
