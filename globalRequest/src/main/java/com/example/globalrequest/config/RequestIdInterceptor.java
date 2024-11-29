package com.example.globalrequest.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/29
 */
@Component
@Slf4j
public class RequestIdInterceptor implements HandlerInterceptor {
    public static final String REQUEST_ID_HEADER = "my-request-Id";
    /**
     * 预处理请求。
     * @param request 客户端发送的HttpServletRequest对象，包含了请求的所有信息。
     * @param response 服务器返回的HttpServletResponse对象，用于设置响应的信息。
     * @param handler 处理器对象，用于处理请求。
     * @return 如果预处理成功，返回true；否则抛出异常。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这里可以添加一些逻辑，比如权限校验、日志记录等
        // 生成requestId
        String requestId = UUID.randomUUID().toString();
        // 将requestId添加到请求属性中，以便后续使用
        request.setAttribute(REQUEST_ID_HEADER, requestId);
        // 将requestId添加到响应头中
        response.setHeader(REQUEST_ID_HEADER, requestId);
        // 如果返回true则继续流程，返回false则中断
        return true;
    }

    /**
     * 在处理器执行后，但在视图渲染之前调用。
     * @param request 客户端的请求对象。
     * @param response 服务器对客户端的响应对象。
     * @param handler 被调用的处理器（Controller）。
     * @param modelAndView 包含模型和视图的容器对象。
     * @throws Exception 如果处理过程中出现异常，则抛出该异常。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        // 这里可以添加一些逻辑，比如设置一些通用模型数据等
        // 从请求属性中获取requestId
        String requestId = (String) request.getAttribute(REQUEST_ID_HEADER);
        // 确保response中包含requestId
        if (response.getHeader(REQUEST_ID_HEADER) == null) {
            response.setHeader(REQUEST_ID_HEADER, requestId);
        }
    }

    /**
     * 在请求处理完成后执行的操作。
     * @param request HttpServletRequest对象，表示客户端的请求。
     * @param response HttpServletResponse对象，表示服务器对客户端的响应。
     * @param handler Object对象，表示处理器（Controller）对象。
     * @param ex Exception对象，如果存在异常，则此参数不为null。
     * @throws Exception 如果处理过程中发生错误，将抛出异常。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        // 这里可以添加一些清理资源的逻辑，比如关闭数据库连接、清除缓存等
    }
}
