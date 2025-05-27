package moe.imtop1.imagehosting.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserInfoService userInfoService;

    @GetMapping("/current")
    public AjaxResult getCurrentUser() {
        String userId = StpUtil.getLoginIdAsString();
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        return AjaxResult.success(userInfo);
    }
} 