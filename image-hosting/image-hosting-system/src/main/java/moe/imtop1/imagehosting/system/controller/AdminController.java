package moe.imtop1.imagehosting.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.common.service.StatsProvider;
import moe.imtop1.imagehosting.common.vo.AdminDashboardStatsVO;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IUserInfoService userInfoService;
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
}
