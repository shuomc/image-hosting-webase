package com.sjy.imagechain.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* com.sjy.imagechain.controller.*.*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 记录请求信息
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String ip = getIpAddress(request);
        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        
        // 打印请求日志
        logger.info("请求开始 >>> URI: {}, Method: {}, IP: {}, Class: {}, Method: {}, Args: {}",
                requestURI, method, ip, className, methodName, Arrays.toString(args));

        // 执行目标方法
        Object result = point.proceed();
        
        // 计算执行时间
        long time = System.currentTimeMillis() - beginTime;
        
        // 打印响应日志
        logger.info("请求结束 >>> URI: {}, Method: {}, Time: {}ms, Result: {}",
                requestURI, method, time, result);
        
        return result;
    }

    /**
     * 获取请求IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 