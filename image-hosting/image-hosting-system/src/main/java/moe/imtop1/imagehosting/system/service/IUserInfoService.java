package moe.imtop1.imagehosting.system.service;

import moe.imtop1.imagehosting.system.domain.UserInfo;

public interface IUserInfoService {
    boolean setPassword(String userEmail, String newPassword);

    boolean isRegistered(String userEmail);

    UserInfo selectUserInfoById(String userId);
}
