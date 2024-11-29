package com.example.globalrequest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/29
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestIdInterceptor())
                .addPathPatterns("/**") // 添加拦截路径，可以指定特定的URL模式
                .excludePathPatterns("/resources/**"); // 排除某些路径，比如静态资源
    }
}
