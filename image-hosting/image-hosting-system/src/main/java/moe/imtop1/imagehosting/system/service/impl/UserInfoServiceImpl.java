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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import moe.imtop1.imagehosting.system.domain.dto.UserListQueryDTO;
import moe.imtop1.imagehosting.system.domain.dto.UserUpdateDTO;
import moe.imtop1.imagehosting.system.domain.vo.UserListVO;
import moe.imtop1.imagehosting.system.domain.vo.UserPageVO;
import org.springframework.beans.BeanUtils;
import java.util.stream.Collectors;

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

    @Override
    public boolean updateUserProfile(UserInfo userInfo) {
        // 1. 获取数据库中的现有用户信息
        UserInfo dbUser = this.getById(userInfo.getUserId());
        if (dbUser == null) {
            return false;
        }

        // 2. 只更新允许修改的字段
        if (userInfo.getNickname() != null) dbUser.setNickname(userInfo.getNickname());
        if (userInfo.getBio() != null) dbUser.setBio(userInfo.getBio());
        if (userInfo.getWebsiteUrl() != null) dbUser.setWebsiteUrl(userInfo.getWebsiteUrl());
        if (userInfo.getPhoneNumber() != null) dbUser.setPhoneNumber(userInfo.getPhoneNumber());
        if (userInfo.getAvatarUrl() != null) dbUser.setAvatarUrl(userInfo.getAvatarUrl());

        // 3. 执行更新
        return this.updateById(dbUser);
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

    @Override
    public UserPageVO getUserList(UserListQueryDTO query) {
        Page<UserInfo> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<UserInfo> wrapper = Wrappers.lambdaQuery();
        
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(UserInfo::getUserName, query.getKeyword())
                   .or()
                   .like(UserInfo::getUserEmail, query.getKeyword());
        }
        
        Page<UserInfo> result = this.page(page, wrapper);
        
        UserPageVO vo = new UserPageVO();
        vo.setTotal(result.getTotal());
        vo.setList(result.getRecords().stream().map(user -> {
            UserListVO item = new UserListVO();
            BeanUtils.copyProperties(user, item);
            return item;
        }).collect(Collectors.toList()));
        
        return vo;
    }

    @Override
    public boolean updateUserStatus(UserUpdateDTO update) {
        UserInfo user = this.getById(update.getUserId());
        if (user == null) return false;
        
        if (update.getUserRole() != null) user.setUserRole(update.getUserRole());
        if (update.getStatus() != null) user.setStatus(update.getStatus());
        
        return this.updateById(user);
    }
}