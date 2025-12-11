package moe.imtop1.imagehosting.system.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final IUserInfoService userInfoService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本项目暂时不需要细粒度的权限控制，只用角色控制
        return Collections.emptyList();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserInfo userInfo = userInfoService.selectUserInfoById(String.valueOf(loginId));
        List<String> list = new ArrayList<>();
        if (userInfo != null && userInfo.getUserRole() != null) {
            list.add(userInfo.getUserRole());
        }
        return list;
    }
}
