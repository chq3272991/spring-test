package com.example.springevent.event.publisher;

import com.example.springevent.event.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/10/28
 */
@Component 
public class MyEventPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;
    
    public void publishEvent(String message) {
        MyEvent event = new MyEvent(this, message);
        publisher.publishEvent(event);
    }
}
