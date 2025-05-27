package com.sjy.imagechain.domain.dto;

import lombok.*;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

public class UserRegistrationRequest {
    private String userId;
    private String userName;
    private String userEmail;
    private String passwordHash;

    public UserRegistrationRequest(String userId, String userName, String userEmail, String passwordHash) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.passwordHash = passwordHash;
    }

    public UserRegistrationRequest() {
    }

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
}
