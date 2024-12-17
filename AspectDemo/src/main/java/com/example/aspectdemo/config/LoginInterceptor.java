package com.example.aspectdemo.config;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/12/17
 * 自定义拦截器，实现简单的session校验
 */
@Component
@Aspect
public class LoginInterceptor {
    
    /**
     * 定义一个切点，用于拦截com.example.aspectdemo.controller包下的所有方法。
     */
    @Pointcut("within(com.example.aspectdemo.controller.*)")
    public void pointCut() {
        
    }
    
    @Around("pointCut()")
    public Object trackInfo(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 放行 td-open 请求过来的接口
        String apiKey = request.getHeader("Open-ApiKey");
        if(apiKey != null && apiKey.equals("123456")) {
            return pjp.proceed();
        }
        
        String sessionId = request.getSession().getId();
        System.out.println("sessionId:" + sessionId);
        // 用户登录时，可以session写入current_user为用户id
        Object sessionUserId = request.getSession().getAttribute("current_user");
        if(sessionUserId != null) {
            return pjp.proceed();
        }
        
        if(apiKey == null || sessionUserId == null) {
            throw new RuntimeException("无权限访问");
        }
        return pjp.proceed();
    }
}
