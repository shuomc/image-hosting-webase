package moe.imtop1.imagehosting.system.service;

import moe.imtop1.imagehosting.system.domain.UserInfo;
import org.apache.catalina.User;

import java.util.List;

public interface IUserInfoService {
    boolean setPassword(String userEmail, String newPassword);

    boolean isRegistered(String userEmail);

    UserInfo selectUserInfoById(String userId);

    List<UserInfo> selectUserInfoList();
}
