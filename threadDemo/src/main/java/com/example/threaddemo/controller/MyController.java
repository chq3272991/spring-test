package com.example.threaddemo.controller;

import com.example.threaddemo.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/12/12
 */
@RestController
public class MyController {
    @Autowired
    private MyService myService;

    @GetMapping("/async-task")
    public String runAsyncTask() throws InterruptedException, ExecutionException {
        myService.performTask2();
        return "Task executed";
    }
}
