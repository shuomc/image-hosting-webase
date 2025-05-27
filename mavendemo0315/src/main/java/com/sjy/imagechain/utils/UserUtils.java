package com.sjy.imagechain.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserUtils {

    @Value("${image-hosting.api-url}")
    private String imageHostingApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public UserUtils(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getCurrentUserId() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                throw new RuntimeException("无法获取请求上下文");
            }

            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("Authorization");

            if (token == null || token.isEmpty()) {
                throw new RuntimeException("缺少 Authorization 请求头");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    imageHostingApiUrl + "/api/user/current",
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());

            if (root != null && root.has("code") && root.get("code").asInt() == 200 && root.has("data")) {
                return root.get("data").get("userId").asText();
            } else {
                System.out.println(root);
                throw new RuntimeException("响应格式不正确或用户未登录");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败: " + e.getMessage(), e);
        }
    }
} 