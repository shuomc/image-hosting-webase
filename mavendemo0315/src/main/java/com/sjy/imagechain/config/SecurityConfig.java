package com.sjy.imagechain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Value("${image-hosting.api-url}")
    private String imageHostingApiUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                String origin = request.getHeader("Origin");
                // 只允许来自图床系统的请求
//                if (origin != null && origin.startsWith(imageHostingApiUrl)) {
//                    return true;
//                }
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                return false;
                return true;
            }
        }).addPathPatterns("/api/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(imageHostingApiUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
} 