package moe.imtop1.imagehosting.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import java.util.List;

public interface IUserInfoService extends IService<UserInfo> {

    boolean setPassword(String userEmail, String newPassword);

    boolean isRegistered(String userEmail);

    UserInfo selectUserInfoById(String userId);

    List<UserInfo> selectUserInfoList();

    // 新增：存储管理
    boolean increaseStorageUsed(String userId, Long size);

    boolean decreaseStorageUsed(String userId, Long size);
}