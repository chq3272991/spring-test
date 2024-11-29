package com.example.springevent.controller;

import com.example.springevent.event.MyEvent;
import com.example.springevent.event.publisher.MyEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/10/28
 */
@RestController
public class IndexController {
    @Autowired
    private MyEventPublisher myEventPublisher;
    
    @GetMapping("/index")
    public String index(@RequestParam String name) {
        System.out.println("name = " + name);
        // 通过myEventPublisher发布一个事件
        myEventPublisher.publishEvent(name);
        return name;
    }
}
