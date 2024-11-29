package com.example.globalrequest.controller;

import com.example.globalrequest.config.RequestIdInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.example.globalrequest.config.RequestIdInterceptor.REQUEST_ID_HEADER;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/29
 */
@RestController
@Slf4j
public class IndexController {
    // 使用@ModelAttribute注解创建一个方法，该方法在每个请求处理之前都会被调用
    @ModelAttribute
    public Map<String, Object> addAttributes(HttpServletRequest request) {
        // 从请求属性中获取my-request-id
        String requestId = (String) request.getAttribute(RequestIdInterceptor.REQUEST_ID_HEADER);
        // 将requestId添加到模型中，以便在所有控制器方法中都可以访问
        HashMap<String, Object> model = new HashMap<>();
        model.put("requestId", requestId);
        return  model;
    }

    @GetMapping("/index")
    public Map<String, Object> index(@RequestParam(defaultValue = "Ming") String name, @ModelAttribute Map<String, String> model) {
        HashMap<String, Object> map = new HashMap<>(model);
        map.put("name", name);
        // 也可以集成ELK，之后通过requestId到elasticsearch中查找
        log.info("调试信息: {}", map);
        return map;
    }
    
    @GetMapping("/index2")
    public Map<String, Object> index2(@RequestParam(defaultValue = "Ming") String name, @ModelAttribute Map<String, String> model) {
        HashMap<String, Object> map = new HashMap<>(model);
        map.put("name", name);
        Boolean isOk = null;
        if(isOk) {
            map.put("a","1");
        }
        log.info("调试信息: {}", map);
        return map;
    }
}
