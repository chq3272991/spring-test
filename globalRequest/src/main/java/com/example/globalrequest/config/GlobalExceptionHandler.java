package com.example.globalrequest.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.globalrequest.config.RequestIdInterceptor.REQUEST_ID_HEADER;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/29
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//    // 处理特定的异常
//    @ExceptionHandler(YourCustomException.class)
//    public ResponseEntity<Object> handleYourCustomException(YourCustomException ex, WebRequest request) {
//        // 构建响应实体
//        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
//        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
//    }

    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
        // 构建响应实体
        //ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        // 打印所有报错信息详情(限测试环境，一般生产环境不能直接返回报错详情信息)
        //return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

        // 生产环境可以将[requestId + 报错信息]写入ELK
        log.error("请求id: {}, 异常信息如下：{}" , request.getAttribute(REQUEST_ID_HEADER), ex.getMessage());
        ex.printStackTrace();
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, String.format("请求requestId:%s , 服务异常", request.getAttribute(REQUEST_ID_HEADER)));
        return new ResponseEntity<>(apiError,new HttpHeaders(), apiError.getStatus());
    }
}
