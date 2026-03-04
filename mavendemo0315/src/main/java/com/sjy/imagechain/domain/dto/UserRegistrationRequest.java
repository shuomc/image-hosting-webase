package com.sjy.imagechain.domain.dto;

import lombok.*;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */
@Data
@NoArgsConstructor // 关键！Jackson 反序列化需要无参构造函数
@AllArgsConstructor
public class UserRegistrationRequest {
    private String userId;
    private String userName;
    private String userEmail;
    private String passwordHash;
    private String avatarUrl;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
