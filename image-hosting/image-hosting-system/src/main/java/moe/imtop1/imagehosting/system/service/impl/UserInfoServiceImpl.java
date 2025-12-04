package moe.imtop1.imagehosting.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.mapper.UserInfoMapper;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户信息服务实现
 * @author shuomc
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    // 由于继承了 ServiceImpl，可以直接使用 baseMapper，也可以使用注入的 userInfoMapper
    private final UserInfoMapper userInfoMapper;

    @Override
    public boolean setPassword(String userEmail, String newPassword) {
        // 调用 userInfoMapper 的 setPassword 方法更新密码
        int affectedRows = userInfoMapper.setPassword(userEmail, newPassword);
        return affectedRows > 0;
    }

    @Override
    public boolean isRegistered(String userEmail) {
        // 调用 userInfoMapper 的 isRegistered 方法查询用户是否存在
        return userInfoMapper.isRegistered(userEmail);
    }

    @Override
    public UserInfo selectUserInfoById(String userId) {
        return userInfoMapper.selectById(userId);
    }

    @Override
    public List<UserInfo> selectUserInfoList() {
        // 查询用户列表，仅返回 ID、用户名、头像、昵称等非敏感信息
        return userInfoMapper.selectList(Wrappers.<UserInfo>lambdaQuery()
                .select(UserInfo::getUserId,
                        UserInfo::getUserName,
                        UserInfo::getNickname,
                        UserInfo::getAvatarUrl));
    }

    // ==========================================
    // 新增：存储配额管理 (需要在 IUserInfoService 接口中定义)
    // ==========================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStorageUsed(String userId, Long size) {
        int rows = userInfoMapper.increaseStorage(userId, size);
        return rows > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseStorageUsed(String userId, Long size) {
        int rows = userInfoMapper.decreaseStorage(userId, size);
        return rows > 0;
    }
}