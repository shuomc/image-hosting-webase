package moe.imtop1.imagehosting.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.mapper.UserInfoMapper;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 修改信息
 * @author shuomc
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo>, IUserInfoService {
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
        return userInfoMapper.selectList(Wrappers.<UserInfo>lambdaQuery()
                .select(UserInfo::getUserId, UserInfo::getUserName));
    }

}
