package com.sjy.imagechain.domain.dto;

import lombok.*;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

@Getter
@Setter
public class UserRegistrationResponse {
    private String userId; // 区块链系统内部的用户ID
    private String userName;
    private String userEmail;
    private String blockchainAddress; // 分配的区块链地址
    private String privateKeyHex;
    private String message; // 注册结果信息
    private boolean success; // 是否成功

    public UserRegistrationResponse(String userId, String userName, String userEmail, String blockchainAddress, String message, boolean success) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.blockchainAddress = blockchainAddress;
        this.message = message;
        this.success = success;
    }

    public UserRegistrationResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
