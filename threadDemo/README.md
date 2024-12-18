# 线程池和异步

## ThreadPoolTaskExecutor 线程池
```java
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
    // CustomTaskDecorator定义
}
```
之后，启动类需要添加注解@EnableAsync，再在Service中可以通过注解@Async("taskExecutor")来使用
```java
@Async("taskExecutor")  // 指定线程池
public void executeAsyncTask() throws InterruptedException {
    Thread.sleep(1000 * 2);
    System.out.println("Task is running in thread: " + Thread.currentThread().getName());
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formatterTime = now.format(formatter);
    System.out.println("当前时间：" + formatterTime);
}
```

## ThreadLocal 
> ThreadLocal 可以与 ThreadPoolTaskExecutor 的 setTaskDecorator 方法结合使用。TaskDecorator 是 Spring 提供的一种机制，
> 用于在任务提交到线程池时，对任务的执行上下文进行包装。这种方式非常适合用于管理 ThreadLocal 的生命周期，确保 ThreadLocal 数据的正确设置和清理。

### 为什么使用TaskDecorator ?
1. 封装逻辑：通过 TaskDecorator，可以在任务执行前后执行额外逻辑，比如设置和清理 ThreadLocal 数据。
2. 避免显式调用：每个任务都无需手动调用 ThreadLocal.remove()，简化代码。
3. 统一管理：通过 TaskDecorator，可以在一个地方统一管理 ThreadLocal 的设置和清理。

```java
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
```
**代码解析**
1. CustomTaskDecorator：
- 捕获任务执行时的上下文信息（如 ThreadLocal 值）。
- 包装任务的逻辑，在任务执行前后对 ThreadLocal 数据进行管理。

2. 在 ThreadPoolTaskExecutor 中设置 TaskDecorator：
- 通过 setTaskDecorator 方法，指定自定义的 TaskDecorator。
 
3. 任务执行流程：
- 任务提交到线程池时，TaskDecorator 会捕获 ThreadLocal 的值。
- 在任务执行时，通过包装逻辑设置和清理 ThreadLocal 数据，确保不会产生数据污染或内存泄漏。

## CompletableFuture异步编程
> CompletableFuture 是 Java 8 中引入的增强版 Future，位于 java.util.concurrent 包中。
> 它的设计目标是帮助开发者更高效地实现异步编程、任务编排以及非阻塞的任务处理。
核心特点：
1. 非阻塞执行：CompletableFuture 支持异步任务的执行和结果处理，避免线程阻塞。
2. 任务链式操作：支持链式调用，可以组合、转换、依赖多个任务。
3. 异常处理：内置异常捕获和处理机制，使得异步任务中的错误易于管理。
4. 并行任务支持：可以组合多个任务的结果，支持并发执行多个异步任务。
### 异步编程，默认线程池
```java
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
```

### 异步编程，异常捕获，自定义线程池
```java
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
```
### 并发编程
```java
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
```