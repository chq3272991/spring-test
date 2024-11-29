package com.example.springevent.listener;

import com.example.springevent.event.MyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/10/28
 */
@Component
public class MyEventListener implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        System.out.println("监听到事件：" + event);
        System.out.println("事件源：" + event.getSource());
        System.out.println("事件消息：" + event.getMessage());
        // todo 执行一些逻辑操作...
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    // 标记为@EventListener，并指定了要监听的事件类型为MyEvent。当MyEvent类型的事件发生时，handleMyEvent方法会被自动调用，并接收到相应的事件对象作为参数。
    @EventListener(MyEvent.class)
    public void handleMyEvent(MyEvent event) {
        System.out.println("处理事件：" + event.getMessage());
    }

}
