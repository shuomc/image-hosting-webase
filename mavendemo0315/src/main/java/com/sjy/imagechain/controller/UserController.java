package com.sjy.imagechain.controller;

import com.sjy.imagechain.domain.dto.UserRegistrationRequest;
import com.sjy.imagechain.domain.dto.UserRegistrationResponse;
import com.sjy.imagechain.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

/**
 * 用户注册接口 (供图床项目调用)
 */
@RestController
@RequestMapping("/api/v1/users") // 接口路径前缀
public class UserController {

    @Autowired
    private AppUserService appUserService;

    /**
     * 接收图床项目发起的注册请求，同步用户信息并分配区块链地址
     * @param request 用户注册信息 (包含图床用户ID, 用户名, 邮箱)
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        // 校验请求体是否包含必要信息
        if (request.getUserId() == null || request.getUserId().isEmpty() || // !!! 校验 userId !!!
                request.getUserName() == null || request.getUserName().isEmpty() ||
                request.getUserEmail() == null || request.getUserEmail().isEmpty() ||
                request.getPasswordHash() == null || request.getPasswordHash().isEmpty()) {
            UserRegistrationResponse response = new UserRegistrationResponse(null, null, null, null, "User ID, username, and email are required.", false);
            return ResponseEntity.badRequest().body(response); // 返回 400 Bad Request
        }

        // 调用 Service 层进行注册处理
        UserRegistrationResponse response = appUserService.registerUserFromImageHosting(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // 返回 200 OK
        } else {
            // 如果 Service 返回失败，根据具体原因返回状态码可能更好
            // 这里简化处理，如果是“已注册”可以返回 200 OK 并附带信息
            // 如果是其他错误（如数据库问题、地址生成失败），则返回 500
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 默认 500
            if ("User already registered.".equals(response.getMessage())) {
                status = HttpStatus.OK; // 如果 Service 特别标记已注册为成功情况，可以返回 200
                // 或者更精确地返回 409 Conflict
                // status = HttpStatus.CONFLICT;
            } else if ("Username or email conflicts with another user.".equals(response.getMessage())) {
                status = HttpStatus.CONFLICT; // 409 Conflict
            }


            return ResponseEntity.status(status).body(response);
        }
    }

}