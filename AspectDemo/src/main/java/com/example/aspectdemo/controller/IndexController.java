package com.example.aspectdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/12/17
 */
@RestController
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "this is api!";
    }
}
