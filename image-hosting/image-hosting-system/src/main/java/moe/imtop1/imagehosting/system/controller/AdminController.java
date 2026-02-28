package moe.imtop1.imagehosting.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.common.service.StatsProvider;
import moe.imtop1.imagehosting.common.vo.AdminDashboardStatsVO;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import moe.imtop1.imagehosting.system.service.IConfigService;
import moe.imtop1.imagehosting.system.service.IUserStatsService;
import moe.imtop1.imagehosting.system.service.IRolesService;
import moe.imtop1.imagehosting.system.domain.dto.AdminUserCreateDTO;
import moe.imtop1.imagehosting.system.domain.dto.AdminRoleCreateDTO;
import moe.imtop1.imagehosting.system.domain.dto.UserListQueryDTO;
import moe.imtop1.imagehosting.system.domain.dto.UserUpdateDTO;
import moe.imtop1.imagehosting.system.domain.vo.UserPageVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IUserInfoService userInfoService;
    private final IConfigService configService;
    private final IUserStatsService userStatsService;
    private final IRolesService rolesService;
    private final List<StatsProvider> statsProviders;

    @SaCheckRole("admin")
    @GetMapping("/stats")
    public AjaxResult getDashboardStats() {
        AdminDashboardStatsVO stats = new AdminDashboardStatsVO();

        // 1. Total Users (Local service)
        stats.setTotalUsers(userInfoService.count());

        // 2. Other stats from providers (Images, NFT)
        if (statsProviders != null) {
            for (StatsProvider provider : statsProviders) {
                provider.fillStats(stats);
            }
        }

        return AjaxResult.success(stats);
    }

    @SaCheckRole("admin")
    @PostMapping("/users/list")
    public AjaxResult getUserList(@RequestBody UserListQueryDTO query) {
        UserPageVO page = userInfoService.getUserList(query);
        return AjaxResult.success(page);
    }

    @SaCheckRole("admin")
    @PutMapping("/users")
    public AjaxResult updateUser(@RequestBody UserUpdateDTO update) {
        boolean success = userInfoService.updateUserStatus(update);
        return success ? AjaxResult.success() : AjaxResult.error("Update failed");
    }

    @SaCheckRole("admin")
    @PostMapping("/users")
    public AjaxResult createUser(@RequestBody AdminUserCreateDTO create) {
        boolean success = userInfoService.createUser(create);
        return success ? AjaxResult.success("User created") : AjaxResult.error("Create failed");
    }

    /**
     * 获取角色列表
     */
    @SaCheckRole("admin")
    @GetMapping("/roles/list")
    public AjaxResult getRolesList() {
        return AjaxResult.success(rolesService.selectRolesList());
    }

    /**
     * 创建角色
     */
    @SaCheckRole("admin")
    @PostMapping("/roles")
    public AjaxResult createRole(@RequestBody AdminRoleCreateDTO roleDto) {
        rolesService.createRole(roleDto);
        return AjaxResult.success("角色创建成功");
    }

    /**
     * 删除角色
     */
    @SaCheckRole("admin")
    @DeleteMapping("/roles/{rolesId}")
    public AjaxResult deleteRole(@PathVariable Integer rolesId) {
        rolesService.deleteRole(rolesId);
        return AjaxResult.success("角色删除成功");
    }

    @SaCheckRole("admin")
    @PostMapping("/users/stats/refresh")
    public AjaxResult refreshUserStats() {
        userStatsService.refreshAllUserStats();
        return AjaxResult.success("用户统计数据已排队更新");
    }

    @SaCheckRole("admin")
    @GetMapping("/users/{userId}/stats")
    public AjaxResult getUserStats(@PathVariable String userId) {
        return AjaxResult.success(userStatsService.selectUserStatsById(userId));
    }

    @SaCheckRole("admin")
    @GetMapping("/config")
    public AjaxResult getConfigs() {
        return AjaxResult.success(configService.getAllConfigs());
    }

    @SaCheckRole("admin")
    @PostMapping("/config")
    public AjaxResult updateConfigs(@RequestBody Map<String, String> configs) {
        configService.updateConfigs(configs);
        return AjaxResult.success();
    }
}
