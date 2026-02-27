package moe.imtop1.imagehosting.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import moe.imtop1.imagehosting.system.service.IUserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserStatsService userStatsService;

    @GetMapping("/current")
    public AjaxResult getCurrentUser() {
        String userId = StpUtil.getLoginIdAsString();
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        return AjaxResult.success(userInfo);
    }

    @PostMapping("/update")
    public AjaxResult updateUserInfo(@RequestBody UserInfo userInfo) {
        String userId = StpUtil.getLoginIdAsString();
        // 强制设置ID为当前登录用户，防止越权修改
        userInfo.setUserId(userId);
        
        boolean result = userInfoService.updateUserProfile(userInfo);
        return result ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }

    @GetMapping("/getUserById")
    public AjaxResult getUserById(String userId) {
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        return AjaxResult.success(userInfo);
    }

    @GetMapping("/getUserList")
    public AjaxResult getUserList() {
        return AjaxResult.success(userInfoService.selectUserInfoList());
    }

    @GetMapping("/dashboard/stats")
    public AjaxResult getUserDashboardStats() {
        String userId = StpUtil.getLoginIdAsString();
        return AjaxResult.success(userStatsService.getUserDashboardStats(userId));
    }
}