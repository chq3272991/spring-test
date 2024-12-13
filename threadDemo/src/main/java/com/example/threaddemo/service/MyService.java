package com.example.threaddemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/12/12
 */
@Service
@Slf4j
public class MyService {

    @Async("taskExecutor")  // 指定线程池
    public void executeAsyncTask() throws InterruptedException {
        Thread.sleep(1000 * 2);
        System.out.println("Task is running in thread: " + Thread.currentThread().getName());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatterTime = now.format(formatter);
        System.out.println("当前时间：" + formatterTime);
    }

    public void executeTask() throws InterruptedException {
        Thread.sleep(1000 * 2);
        System.out.println("Task is running in thread: " + Thread.currentThread().getName());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatterTime = now.format(formatter);
        System.out.println("当前时间：" + formatterTime);
    }

    // CompletableFuture 提供异步任务处理，默认线程池
    public void performTask() throws InterruptedException, ExecutionException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() ->{
            log.info("任务运行在线程：" + Thread.currentThread().getName());
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // 主线程等待任务完成(可选)
        //future.join();
        // 异常获取
        //future.get();
        //log.info("任务完成");
        log.info("任务已在处理中，稍后查看任务");
    }

    // CompletableFuture 提供异步任务处理， 自定义线程池
    // curl http://localhost:8080/async-task可以看到异常捕获效果
    public void performTask2() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CompletableFuture.runAsync(() ->{
            log.info("任务运行在线程：" + Thread.currentThread().getName());
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 模拟异常
            if (true) {
                throw new RuntimeException("任务中出现异常！");
            }
        },executorService).exceptionally(ex -> {
            log.info("捕获异常：" + ex.getMessage());
            return null;
        });

        log.info("任务已在处理中，稍后查看任务");
        // 关闭线程池
        // executorService.shutdown();
    }
}
