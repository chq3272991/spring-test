package com.example.threaddemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/12/12
 */
@Service
@Slf4j
public class MyService {

    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

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
        // ExecutorService executorService = Executors.newFixedThreadPool(10);

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
        }, taskExecutor).exceptionally(ex -> {
            log.info("捕获异常：" + ex.getMessage());
            return null;
        });

        log.info("任务已在处理中，稍后查看任务");
        // 关闭线程池
        // executorService.shutdown();
    }

    // 并行执行多个任务并处理结果。
    public void performTask3() throws InterruptedException, ExecutionException {
        log.info("开始测试并发任务1(耗时2秒)、并发任务2、并发任务3(耗时3秒)");
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return  "Task 1";
        });
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> "Task 2");
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return  "Task 3";
        });

        CompletableFuture<Void> combined = CompletableFuture.allOf(task1, task2, task3);
        combined.thenRun(() -> {
            try {
                String result1 = task1.join(); // 使用 join 确保任务完成并获取结果
                String result2 = task2.join();
                String result3 = task3.join();
                log.info(result1 + " & " + result2 + " & " + result3 );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).join(); // 等待 combined 完成

        log.info("结束测试并发任务1(耗时2秒)、并发任务2、并发任务3(耗时3秒)，总耗时3秒");
    }
}
