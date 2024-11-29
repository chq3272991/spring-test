package com.example.springevent.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/10/28
 */
public class MyEvent extends ApplicationEvent {
    private String message;

    public MyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
