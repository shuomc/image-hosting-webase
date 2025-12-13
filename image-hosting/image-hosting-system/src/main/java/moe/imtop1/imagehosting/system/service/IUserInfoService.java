package moe.imtop1.imagehosting.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import java.util.List;

import moe.imtop1.imagehosting.system.domain.dto.UserListQueryDTO;
import moe.imtop1.imagehosting.system.domain.dto.UserUpdateDTO;
import moe.imtop1.imagehosting.system.domain.vo.UserPageVO;

public interface IUserInfoService extends IService<UserInfo> {

    boolean setPassword(String userEmail, String newPassword);

    boolean isRegistered(String userEmail);

    UserInfo selectUserInfoById(String userId);

    List<UserInfo> selectUserInfoList();

    /**
     * 更新用户个人资料
     * @param userInfo 包含更新信息的对象
     * @return 是否更新成功
     */
    boolean updateUserProfile(UserInfo userInfo);

    // 新增：存储管理
    boolean increaseStorageUsed(String userId, Long size);

    boolean decreaseStorageUsed(String userId, Long size);

    UserPageVO getUserList(UserListQueryDTO query);

    boolean updateUserStatus(UserUpdateDTO update);
}